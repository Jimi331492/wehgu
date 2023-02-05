/*
 * @Author: 龙际妙
 * @Date: 2022-05-19 05:47:31
 * @Description:
 * @FilePath: \wehgu-vue-admin\src\api\auth.js
 * @LastEditTime: 2022-05-19 07:24:32
 * @LastEditors: Please set LastEditors
 */
import request from '../utils/request'

// 示例
export function query(data) {
  //传数据写入data，不传则不需要
  return request({
    url: '/audit/getAuthList',
    method: 'post',
    data,
  })
}

// 示例
export function authSuccess(data) {
  //传数据写入data，不传则不需要
  return request({
    url: '/audit/batchAuthSuccess',
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

// 示例
export function save(data) {
  //传数据写入data，不传则不需要
  return request({
    url: `/order/deleteOrder?UID=${data}`,
    method: 'delete',
    data,
  })
}

// // 示例
// export function save(data) {
//   //传数据写入data，不传则不需要
//   return request({
//     url: `/order/deleteOSS?UID=${data}`,
//     method: 'delete',
//     data,
//   })
// }
