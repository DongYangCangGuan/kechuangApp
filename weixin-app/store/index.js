const Jstore = require('./jstore');
const state = require('./state');
const mutations = require('./mutations');
const actions = require('./actions');
const getters = require('./getters');

module.exports = new Jstore({
    state,
    mutations,
    actions,
    getters
});
