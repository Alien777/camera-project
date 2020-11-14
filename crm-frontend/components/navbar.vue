<template>
    <div>
        <b-navbar fixed="top" toggleable="lg" type="light" variant="info">
            <b-navbar-brand href="/">
                CRM
            </b-navbar-brand>
            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav class="ml-auto">
                    <b-button variant="info" href="/singin">{{ $t('singin') }}</b-button>
                    <b-button variant="info" href="/singup">{{ $t('singup') }}</b-button>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>
    </div>
</template>