
// 新增接口
const addBalance = (params) => {
  return $axios({
    url: '/details',
    method: 'post',
    data: { ...params }
  })
}