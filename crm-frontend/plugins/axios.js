export default function ({$axios, store, redirect}) {

  $axios.interceptors.response.use(function (response) {
    return response;
  }, function (error) {
    if (401 === error.response.status) {
      redirect("/logout")
    } else {
      return Promise.reject(error);
    }
  });

  $axios.onRequest((config) => {
    if (store.state.auth !== null) {
      config.headers.common['authorization'] = store.state.auth.token;
      config.headers.common['user_id'] = store.state.auth.id;
    }
  })
}
