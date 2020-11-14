<template>
    <div>
        <b-container class="text-light">
            <b-row class="justify-content-md-center">
                <b-col cols="12" md="auto" class="mt-5">
                    <div role="group" class="mt-4">
                        <b-form-input id="name" v-model="form.name" :state="nameState"
                                      :placeholder="$t('login.nameP')" trim/>
                    </div>
                    <div role="group" class="mt-4">
                        <b-form-input id="email" v-model="form.email" :state="emailState"
                                      :placeholder="$t('login.emailP')" trim/>
                    </div>
                    <div role="group" class="mt-4">
                        <b-form-input type="password" id="pass" v-model="form.pass" :state="passwordState"
                                      :placeholder="$t('login.passwordP')" trim/>
                    </div>
                    <b-button @click="registry" class="mt-4 align-content-center w-100"
                              :disabled='!emailState || !passwordState || !nameState'
                              variant="info">{{ $t('singup') }}
                    </b-button>
                </b-col>
            </b-row>
        </b-container>
    </div>
</template>
<script>
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    export default {
        middleware: 'notAuthenticated',
        data() {
            return {
                form: {
                    name: '',
                    email: '',
                    pass: ''
                }
            }
        },
        methods: {
            registry() {
                console.log(this.form)
                this.$axios.$post('/api/user/register', this.form).then(value => {
                    this.$bvToast.toast(this.$t('login.successDescriptionRegistry'), {
                        title: this.$t('login.successTitleRegistry'),
                        toaster: 'b-toaster-bottom-center',
                        variant: "success",
                    });
                }).catch(reason => {
                    this.$bvToast.toast(this.$t('login.errorDescriptionRegistry'), {
                        title: this.$t('login.errorTitleRegistry'),
                        autoHideDelay: 5000,
                        toaster: 'b-toaster-bottom-center',
                        variant: "danger",
                    })
                }).then(value => {
                    this.form.email = '';
                    this.form.name = '';
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
            },
            nameState() {
                if (this.form.name === '') {
                    return null;
                }
                return this.form.name.length > 1
            }
        },
    }

</script>
