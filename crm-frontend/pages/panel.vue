<template>
    <div>

        <b-navbar fixed="top" toggleable="lg" type="light" variant="info">
            <b-navbar-brand href="/">
                CRM
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item v-on:click="change('camera')" href="#">Moje monitoringi</b-nav-item>
                    <b-nav-item href="#">Nowy monitoring</b-nav-item>
                </b-navbar-nav>
            </b-collapse>
            <b-navbar-nav class="ml-auto">
                <b-button variant="info" v-on:click="change('logout')">
                    <b-icon style="color: red" icon="power" font-scale="2"></b-icon>
                </b-button>
            </b-navbar-nav>
        </b-navbar>
        <b-container>
            <keep-alive>
                <component v-bind:is="currentTabComponent"></component>
            </keep-alive>
        </b-container>
    </div>

</template>
<script>
    import Vue from 'vue'
    import {BootstrapVueIcons} from 'bootstrap-vue'

    Vue.use(BootstrapVueIcons);

    const Cookie = process.client ? require('js-cookie') : undefined
    const camera = () => import('../components/panel/camera.vue');


    export default {
        components: {
           camera
        },
        data() {
            return {
                currentTabComponent: camera
            }
        },

        layout: 'panel',
        middleware: 'authenticated',
        methods: {
            change(component) {

                if (component === 'camera') {
                    this.currentTabComponent = camera;
                }
                if (component === 'logout') {
                    Cookie.remove('auth')
                    this.$store.commit('setAuth', null)
                    this.$router.push('/')
                }
            }
        }
    }
</script>
