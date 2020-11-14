<template>
    <b-input-group :prepend="title">
        <b-form-input @keydown.enter.native="saveAction" :type='type' @keydown.esc.native="cancelAction"
                      :state="stateField"
                      :aria-describedby="id" v-model="valueTemp"></b-form-input>
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
    </b-input-group>
</template>
<script>
    import {uuid} from 'vue-uuid';

    export default {
        props: {
            value: {
                type: String,
                default: null
            },
            type: {
                type: String,
                default: 'text'
            },
            title: {
                type: String,
                default: "pole"
            },
            message: {
                type: String,
                default: "Niepoprawna wartość."
            },
            max: {
                type: Number,
                default: 9999999
            },
            min: {
                type: Number,
                default: -1
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
                    return this.valueTemp.length > this.min && this.valueTemp.length < this.max;
                }
            },
        computed: {
            cancelHidden() {
                return (this.value === this.valueTemp);
            },
            saveHidden() {
                return (this.value === this.valueTemp);
            },
            stateField() {
                return this.valid();
            }
        }
    }
</script>