<template>
</template>
<script>

    export default {
        middleware: 'notAuthenticated',
        mounted: function () {

            this.$axios.$get('/api/user' + $nuxt.$route.path).then(value => {
                this.$bvToast.toast(this.$t('activateTitle'), {
                    title: this.$t('activate'),
                    href: 'http://localhost:3000/singin',
                    toaster: 'b-toaster-top-full',
                    variant: "success",
                    appendToast: false
                });
            }).catch(reason => {
                this.$bvToast.toast(this.$t('noActivateTitle'), {
                    title: this.$t('noActivate'),
                    toaster: 'b-toaster-top-full',
                    variant: "danger",
                    appendToast: false
                })
            })
        }
    }

</script>
