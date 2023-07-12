import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/yxStoreCouponIssue',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/yxStoreCouponIssue/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/yxStoreCouponIssue',
    method: 'put',
    data
  })
}

export function batchSendCoupon(data) {
  return request({
    url: 'api/coupon/batchSendCoupon',
    method: 'post',
    data
  })
}
