/*
 * @Author: 龙际妙
 * @Date: 2022-05-18 21:57:26
 * @Description:
 * @FilePath: \wehgu-vue-admin\src\api\oss.js
 * @LastEditTime: 2022-05-18 23:31:33
 * @LastEditors: Please set LastEditors
 */
import request from '../utils/request'
// 示例
export function query(data) {
  //传数据写入data，不传则不需要
  return request({
    url: '/sys_oss/getOSSPage',
    method: 'post',
    data,
  })
}

// 示例
export function remove(data) {
  //传数据写入data，不传则不需要
  return request({
    url: `/order/deleteOSS?UID=${data}`,
    method: 'delete',
    data,
  })
}
