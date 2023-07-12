<template>
  <div class="login">
    <div class="bubbles">
      <img class="bubble" src="../assets/images/bubble.png">
      <img class="bubble" src="../assets/images/bubble.png">
      <img class="bubble" src="../assets/images/bubble.png">
      <img class="bubble" src="../assets/images/bubble.png">
      <img class="bubble" src="../assets/images/bubble.png">
    </div>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-position="left" label-width="0px" class="login-form">
      <div class="form-wrap">
        <h3 class="title">
          用户登录
        </h3>
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码" @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码" style="width: 50%" @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code">
            <img :src="codeUrl" @click="getCode">
          </div>
        </el-form-item>
        <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;">
          记住我
        </el-checkbox>
        <el-form-item style="width:100%;">
          <el-button :loading="loading" size="medium" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
        </el-form-item>
      </div>
      <div class="form-bg">
        <img class="bg-icon" src="../assets/images/icon.png" />
        <span class="bg-brand">小象农庄后台管理系统</span>
        <!-- <span class="bg-title">小象农庄后台管理系统</span> -->
      </div>
    </el-form>
    <!--  底部  -->
    <div v-if="$store.state.settings.showFooter" id="el-login-footer">
      <span v-html="$store.state.settings.footerTxt" />
      <span> ⋅ </span>
      <a href="https://beian.miit.gov.cn" target="_blank">{{ $store.state.settings.caseNumber }}</a>
    </div>
  </div>
</template>

<script>
import { encrypt } from '@/utils/rsaEncrypt'
import Config from '@/settings'
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
export default {
  name: 'Login',
  data() {
    return {
      codeUrl: '',
      cookiePass: '',
      loginForm: {
        username: '',
        password: '',
        rememberMe: false,
        code: '',
        uuid: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
        code: [{ required: true, trigger: 'change', message: '验证码不能为空' }]
      },
      loading: false,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.codeUrl = res.img
        this.loginForm.uuid = res.uuid
      })
    },
    getCookie() {
      const username = Cookies.get('username')
      let password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      // 保存cookie里面的加密后的密码
      this.cookiePass = password === undefined ? '' : password
      password = password === undefined ? this.loginForm.password : password
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password,
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
        code: ''
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        const user = {
          username: this.loginForm.username,
          password: this.loginForm.password,
          rememberMe: this.loginForm.rememberMe,
          code: this.loginForm.code,
          uuid: this.loginForm.uuid
        }
        if (user.password !== this.cookiePass) {
          user.password = encrypt(user.password)
        }
        if (valid) {
          this.loading = true
          if (user.rememberMe) {
            Cookies.set('username', user.username, { expires: Config.passCookieExpires })
            Cookies.set('password', user.password, { expires: Config.passCookieExpires })
            Cookies.set('rememberMe', user.rememberMe, { expires: Config.passCookieExpires })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch('Login', user).then(() => {
            this.loading = false
            this.$router.push({ path: this.redirect || '/' })
          }).catch(() => {
            this.loading = false
            this.getCode()
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-image:url("../assets/images/bg.jpg");
    background-size: cover;

    position: absolute;
    z-index: 0;
    overflow: hidden;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
  }
  .bubble {
    position: absolute;
    bottom: -100px;
    opacity: 0.5;
    animation:  flying  10s infinite ease-in;
  }
  .bubble:nth-child(1){
    width: 40px;
    height: 40px;
    left: 65%;
    animation-duration: 5s;
    animation-delay: 1s;
  }
  .bubble:nth-child(2){
    width: 40px;
    height: 40px;
    left: 10%;
    animation-duration: 5s;
    animation-delay: 3s;
  }
  .bubble:nth-child(3){
    width: 60px;
    height: 60px;
    left: 30%;
    animation-duration: 4s;
    animation-delay: 6s;
  }
  .bubble:nth-child(4){
    width: 20px;
    height: 20px;
    left: 50%;
    animation-duration: 8s;
    animation-delay: 2s;
  }
  .bubble:nth-child(5){
    width: 80px;
    height: 80px;
    left: 80%;
    animation-duration: 5s;
    animation-delay: 4s;
  }
  @keyframes flying {
    0% {
      bottom: -100px;
      transform: translateX(0);
    }
    50% {
      transform: translateX(-100px);
    }
    100% {
      bottom: 100%;
      transform: translateX(100px);
    }
  }

  .title {
    margin: 0 auto 30px auto;
    text-align: center;
    color: #707070;
  }

  .login-form {
    border-radius: 6px;
    background: #ffffff;
    width: 50%;
    display: flex;
    .el-input {
      height: 38px;
      input {
        height: 38px;
      }
    }
    .input-icon{
      height: 39px;width: 14px;margin-left: 2px;
    }
    .form-wrap {
      margin: 25px;
      width: 40%;
    }
    .form-bg {
      width: 60%;
      height: auto;
      background-image:url("../assets/images/aside.jpg");
      background-size: cover;
      background-color: cadetblue;
      background-size: 100% 100%;
      background-repeat: no-repeat;
      border-radius: 0 6px 6px 0;
      display: -webkit-box;
      display: -ms-flexbox;
      display: flex;
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
      color: #fff;
      justify-content: center;
      flex-direction: column;
      font-weight: bolder;

      .bg-icon {
        width: 16em;
      }
      .bg-brand {
        font-size: 1.75em;
      }
      .bg-title {

      }
    }

  }
  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }
  .login-code {
    width: 50%;
    display: inline-block;
    height: 38px;
    float: right;
    text-align: right;
    img{
      cursor: pointer;
      vertical-align:middle
    }
  }
  @media (max-width: 1024px) {
        .login-form{
            width: 100%;
            max-width: 320px;
            position: relative;
            .form-wrap{
                width: 100%;
            }
        }
        .login-form .form-bg{
            position: absolute;
            width: 100%;
            left: 0;
            top: -65px;
            background: none;
            .bg-icon{
                display: none;
            }
        }
}
</style>
