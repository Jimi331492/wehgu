/*
 * @Author: 龙际妙
 * @Date: 2022-05-19 22:35:28
 * @Description:
 * @FilePath: \wehgu-vue-admin\src\api\notice.js
 * @LastEditTime: 2022-05-19 22:35:41
 * @LastEditors: Please set LastEditors
 */
import request from '../utils/request'

// 示例
export function query(data) {
  //传数据写入data，不传则不需要
  return request({
    url: '/notice/getNoticePage',
    method: 'post',
    data,
  })
}
// 示例
export function save(data) {
  //传数据写入data，不传则不需要
  return request({
    url: '/notice/saveNotice',
    method: 'post',
    data,
  })
}

// 示例
export function remove(data) {
  //传数据写入data，不传则不需要
  return request({
    url: `/notice/deleteNotice?UID=${data}`,
    method: 'delete',
    data,
  })
}
