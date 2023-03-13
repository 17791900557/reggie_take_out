const reduceBalance = (params) => {
  return $axios({
    url: '/details/reduce',
    method: 'post',
    data: { ...params }
  })
}