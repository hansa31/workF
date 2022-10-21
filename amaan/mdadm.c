#include <stdio.h>
#include <string.h>
#include <assert.h>

#include "mdadm.h"
#include "jbod.h"

int is_mounted = 0;
int is_written = 0;


//function to get the opcode
uint32_t getOPCode(int disk_id, int block_id, jbod_cmd_t cmd){
  return (block_id & 0xff)|((disk_id & 0xff)<<8)|((cmd)<<12);
}

/*
int jbod_operation(uint32_t op, uint8_t *block);

This function returns 0 on success and -1 on failure. It accepts an operation through the op parameter, 
the
format of which is described in Table 1, and a pointer to a buffer. The command field can be one of the
following commands, which are declared as a C enum type in the header that we have provide to you:

*/


/*
Mount the linear device; 
now mdadm user can run read and
oper- ations on the linear address space that combines all disks.
 It should return 1 on success and
-1 on failure. 
Calling this function the second time without calling mdadm_unmount in between, should
fail.
*/
int mdadm_mount(void) {

  // if is_mounted is 0 retrun failure.
  if(is_mounted){
    return -1;
  }

  /*
  JBOD_MOUNT: mount all disks in the JBOD and make them ready to serve commands. This is the first
  command that should be called on the JBOD before issuing any other commands; all commands before
  it will fail. When the command field of op is set to this command, all other fields in op 
  are ignored by
  the JBOD driver. Similarly, the block argument passed to jbod_operation can be NULL. 
  */

  // if mount is unsucessfull return failure
  if (jbod_operation((JBOD_MOUNT<<12),NULL)==-1){
    return -1;
  }

  //mount successful
  is_mounted = 1;
  return 1;

}

int mdadm_unmount(void) {

  // if is_mounted is 1 return failure.
  if(!is_mounted){
    return -1;
  }

  //if unmount is unsuceccfull return -1
  if(jbod_operation((JBOD_UNMOUNT<<12),NULL)==-1){
    return -1;
  }
  
  //unmount successful
  is_mounted = 0;
  return 1;
 
}

int mdadm_write_permission(void){
 
  return 0;
}


int mdadm_revoke_write_permission(void){
	return 0;
}

void readFrom(int disk_id, int block_id, int offset, uint32_t read_len, uint8_t *read_buf){

  uint8_t temp_buf[JBOD_BLOCK_SIZE];

  //seek to disk and block

  jbod_operation(getOPCode(disk_id,0,JBOD_SEEK_TO_DISK),NULL);
  jbod_operation(getOPCode(0,block_id,JBOD_SEEK_TO_BLOCK),NULL);

  //read a block temporarily

  jbod_operation(getOPCode(0,0,JBOD_READ_BLOCK),temp_buf);

  block_id++;

  //if reading started anywhere after position 0

  uint32_t read_size = JBOD_BLOCK_SIZE - offset;
  
  if(offset == 0 && read_len < JBOD_BLOCK_SIZE){
    read_size = read_len;                   // read starts from 0 and size is less than block size
  }
  memcpy(read_buf,temp_buf + offset, read_size);

  //Current position after reading the size

  uint32_t curr_pos = read_len-read_size;
  while(curr_pos > JBOD_BLOCK_SIZE){
    jbod_operation(getOPCode(0,block_id,JBOD_SEEK_TO_BLOCK),NULL);
    
    jbod_operation(getOPCode(0,0,JBOD_READ_BLOCK),temp_buf);
    block_id++;

    //write after the read size

    memcpy(read_buf + read_size,temp_buf,JBOD_BLOCK_SIZE);

    //to update the read size and position

    read_size += JBOD_BLOCK_SIZE;
    curr_pos -= JBOD_BLOCK_SIZE;

  }
  if(curr_pos > 0){     //if there is still some data to read
    jbod_operation(getOPCode(0,block_id,JBOD_SEEK_TO_BLOCK),NULL);
    jbod_operation(getOPCode(0,0,JBOD_READ_BLOCK),temp_buf);
    memcpy(read_buf + read_size,temp_buf,curr_pos);
  }
}


int mdadm_read(uint32_t start_addr, uint32_t read_len, uint8_t *read_buf)  {

  int max_len = 2048;
  int max_addr = JBOD_NUM_DISKS * JBOD_DISK_SIZE;

  if(!is_mounted){
    return -1;
  }
  if(read_buf == NULL && read_len != 0){
    return -1;
  }
  if(read_buf && read_len ==0){
    return -1;
  }
  if(read_len > max_len){
    return -1;
  }
  if(read_len + start_addr > max_addr){
    return -1;
  }

  int disk_addr = start_addr % JBOD_DISK_SIZE;
  int disk_id = start_addr / JBOD_DISK_SIZE;
  int block_id = disk_addr/JBOD_BLOCK_SIZE;
  int offset = disk_addr % JBOD_BLOCK_SIZE;

  if(disk_addr + read_len <= JBOD_DISK_SIZE){
    readFrom(disk_id,block_id,offset,read_len,read_buf);
  }
  else{
    int len_from_disk = JBOD_DISK_SIZE - disk_addr;  //read from multiple disks

    readFrom(disk_id,block_id,offset,len_from_disk,read_buf);
    disk_id++;

    uint32_t read_size = len_from_disk;
    uint32_t read_remain = read_len - len_from_disk;
    while(read_remain > JBOD_DISK_SIZE){

      //read from next disks
      readFrom(disk_id,0,0,JBOD_DISK_SIZE,read_buf + read_size);

      read_size += JBOD_DISK_SIZE;
      read_remain -= JBOD_DISK_SIZE;
      disk_id++;

    }
    if(read_remain > 0){
      readFrom(disk_id,0,0,read_remain,read_buf + read_size);
    }
  }
  return (int) read_len;

  
	return 0;
}



int mdadm_write(uint32_t start_addr, uint32_t write_len, const uint8_t *write_buf) {
	return 0;
}

