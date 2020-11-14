<template>
    <div>
        <navbar/>
        <nuxt class="mt-5 mb-5  mb-5"/>
        <b-navbar fixed="bottom" toggleable="lg" type="light" variant="info">
            {{ $t('comminicate') }}
        </b-navbar>
    </div>
</template>
<script>
    import navbar from '~/components/navbar.vue'
    export default {
        components: {
            navbar
        }
    }
</script>