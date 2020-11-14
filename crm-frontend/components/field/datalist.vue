<template>
    <b-input-group>
        <template v-slot:prepend>
            <b-input-group-text>
                <b-icon variant="success-outline" :icon="title"></b-icon>
            </b-input-group-text>
        </template>

        <b-form-input @keydown.esc.native="cancelAction"
                      @keydown.enter.native="saveAction"
                      v-model="valueTemp"
                      :list="id+'list'"
                      :id="id" :state="stateField"></b-form-input>
        <b-form-datalist :id="id+'list'"
                         :options="options"></b-form-datalist>
        <b-input-group-append>
            <b-button @click="saveAction" :hidden='saveHidden || !stateField' variant="outline-success">
                <b-icon icon="check"></b-icon>
            </b-button>
            <b-button @click="cancelAction" :hidden='cancelHidden' variant="outline-danger">
                <b-icon icon="arrow-counterclockwise"></b-icon>
            </b-button>
        </b-input-group-append>
        <b-form-invalid-feedback :id="id">
            {{message}}
        </b-form-invalid-feedback>
        <b-tooltip v-if="tooltip!=null" triggers="hover" :target="id" variant="secondary">{{tooltip}}</b-tooltip>
    </b-input-group>
</template>
<script>
    import {uuid} from 'vue-uuid';

    export default {
        props: {
            contains: {
                type: Boolean,
                default: true
            },
            value: {
                type: String,
                default: null
            },
            options: {
                type: Array,
                default: []

            },
            title: {
                type: String,
                default: "pole"
            },
            tooltip: {
                type: String,
                default: null
            },
            message: {
                type: String,
                default: "Niepoprawna wartość."
            }
        },
        data() {
            return {
                id: uuid.v4(),
                valueTemp: this.value,
            }
        },
        methods:
            {
                cancelAction() {
                    this.valueTemp = this.value;
                },
                saveAction() {
                    if (this.valid() !== false) {
                        this.$emit('input', this.valueTemp)
                    }
                },
                valid() {
                    if (this.valueTemp === this.value) {
                        return null;
                    }
                    if (this.valueTemp === null) {
                        return null;
                    }
                    return this.contains === true && this.options.find(value => value === this.valueTemp) !== undefined;
                }
            },
        computed: {
            cancelHidden() {
                return (this.value === this.valueTemp);
            },
            saveHidden() {
                return this.value === this.valueTemp;
            },
            stateField() {
                return this.valid();
            }
        }
    }
</script>