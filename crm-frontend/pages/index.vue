<template>
    <div>
        <div class="bg-info">
            <b-container class="text-light">
                <b-row class="p-3">
                    <b-col>
                        <h1 style="width: 400px">{{ $t('main') }}</h1>
                        <hr class="my-4">
                        <p>{{ $t('submain') }}</p>
                    </b-col>
                    <b-col center class="mt-5">
                        <b-img-lazy center alt="Center image" src="~/static/selfhouse.png" width="300px"></b-img-lazy>
                    </b-col>
                </b-row>
            </b-container>
        </div>
        <b-container class="mt-5">
            <b-row class="p-3">
                <b-col class="rounded-1">
                    <h2>{{ $t('recordCameraTitle') }} </h2>
                    <p>{{ $t('recordCameraDescription') }}</p>
                </b-col>
                <b-col>
                    <b-img-lazy center alt="Center image" src="~/static/laptopcamera.png" width="300px"></b-img-lazy>
                </b-col>
            </b-row>
        </b-container>

    </div>
</template>
<script>
    export default {
        middleware: 'notAuthenticated'
    }
</script>