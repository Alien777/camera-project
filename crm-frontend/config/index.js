import pl from '../lang/pl-PL.js'

export const API_ROOT = 'https://jsonplaceholder.typicode.com/'

export const I18N = {
    locales: [
        {
            code: 'pl',
            iso: 'pl-PL',
            name: 'Polski'
        }
    ],
    defaultLocale: 'pl',

    vueI18n: {
        fallbackLocale: 'pl',
        messages: {pl}
    }
}