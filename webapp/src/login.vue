<template>
    <div>
        <div class="main-login">
            <div class="container">
                <center>
                    <div class="middle">
                        <div id="login">
                            <form>
                                <fieldset class="clearfix">
                                    <p>
                                        <span class="fa fa-user"></span>
                                        <input type="text" Placeholder="Username" required v-model="name">
                                    </p>
                                    <!-- JS because of IE support; better: placeholder="Username" -->
                                    <p>
                                        <span class="fa fa-lock"></span>
                                        <input type="password" Placeholder="Password" required v-model="password">
                                    </p>
                                    <!-- JS because of IE support; better: placeholder="Password" -->

                                    <div>
                                        <button class="btn btn-success btn-width" type="button" @click="login">Login</button>
                                        <!--<span style="width:48%; text-align:left;  display: inline-block;">
                                        </span>
                                        <span style="width:50%; text-align:right;  display: inline-block;">

                                        </span>-->
                                    </div>
                                </fieldset>
                                <div class="clearfix"></div>
                            </form>

                            <div class="clearfix"></div>

                        </div> <!-- end login -->
                        <div class="logo">LOGO

                    <div class="clearfix"></div>
                        </div>

                    </div>
                </center>
            </div>

        </div>
    </div>
</template>
<script>
    import crypto_js from 'crypto-js';
    export default {
        data () {
            return {
                name: '',
                password: '',
            }
        },
        methods: {
            login: function () {
                let _this = this;
                let data = {
                    name:_this.name,
                    password: _this.password
                };
                sessionStorage.setItem('user', JSON.stringify(data));
                _this.$router.push({path: '/dashboard'})
            }
        }
    }
</script>