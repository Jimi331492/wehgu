/*
 * @Author: 龙际妙
 * @Date: 2022-05-18 03:01:50
 * @Description:
 * @FilePath: \wehgu-vue-admin\src\api\order.js
 * @LastEditTime: 2022-05-18 03:05:11
 * @LastEditors: Please set LastEditors
 */
import request from '../utils/request'

// 示例
export function query(data) {
  //传数据写入data，不传则不需要
  return request({
    url: '/order/getOrderPage',
    method: 'post',
    data,
  })
}
// 示例
export function save(data) {
  //传数据写入data，不传则不需要
  return request({
    url: '/order/saveOrder',
    method: 'post',
    data,
  })
}

// 示例
export function remove(data) {
  //传数据写入data，不传则不需要
  return request({
    url: `/order/deleteOrder?UID=${data}`,
    method: 'delete',
    data,
  })
}
