<template>
    <div>
        <b-container class="text-light">
            <b-row class="justify-content-md-center">
                <b-col cols="12" md="auto" class="mt-5">
                    <div role="group" class="mt-4">
                        <b-form-input id="email" v-model="form.email" :state="emailState"
                                      :placeholder="$t('login.emailP')" trim/>
                    </div>
                    <div role="group" class="mt-4">
                        <b-form-input type="password" id="pass" v-model="form.pass" :state="passwordState"
                                      :placeholder="$t('login.passwordP')" trim/>
                    </div>
                    <b-button @click="login" class="mt-4 align-content-center w-100"
                              :disabled='!emailState || !passwordState'
                              variant="info">{{ $t('singin') }}
                    </b-button>
                </b-col>
            </b-row>
        </b-container>
    </div>
</template>
<script>
    const Cookie = process.client ? require('js-cookie') : undefined
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    export default {
        middleware: 'notAuthenticated',
        data() {
            return {
                form: {
                    email: '',
                    pass: ''
                }
            }
        },
        methods: {
            login() {
                this.$axios.$post('/api/user/login', this.form)
                    .then(value => {
                        const auth = value;
                        this.$store.commit('setAuth', auth) // mutating to store for client rendering
                        Cookie.set('auth', auth) // saving token in cookie for server rendering
                        console.log(auth)
                        this.$router.push('/panel')
                    })

                    .catch(reason => {
                        this.$bvToast.toast(this.$t('login.errorDescription'), {
                            title: this.$t('login.errorTitle'),
                            autoHideDelay: 5000,
                            toaster: 'b-toaster-bottom-center',
                            variant: "danger",
                        })
                    }).then(value => {
                    this.form.email = '';
                    this.form.pass = '';
                })
            }
        },
        computed: {
            emailState() {
                if (this.form.email === '') {
                    return null;
                }
                return this.form.email.toLowerCase().match(re) !== null;
            },
            passwordState() {
                if (this.form.pass === '') {
                    return null;
                }
                return this.form.pass.length > 1
            }
        },
    }

</script>
