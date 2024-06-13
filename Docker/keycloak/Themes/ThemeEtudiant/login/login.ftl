<#import "template.ftl" as layout>
<link rel="stylesheet" href="${url.resourcesPath}/css/login.css">
<link rel="stylesheet" href="${url.resourcesPath}/css/bootstrap.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.5.0/font/bootstrap-icons.min.css">


<div class="container-fluid">  

    

  

        



            <div class="row" style="height: 100vh;">
            
                <div class="gauche col-12 col-md-7 d-flex flex-column justify-content-center align-items-center d-none  d-md-flex "  style="background-image: url('${url.resourcesPath}/images/DesktopBG.png'); background-size: cover;" >


                    <div class="d-flex justify-content-center" >
                        <img src="${url.resourcesPath}/images/logoUgb.png" alt=" logoUGB" srcset="">
                    </div>

                        <div class="">
                            <h1 class="text-center text-white size-title" style=" color: #B25F35 !important ; ">Espace d’authentification</h1>
                        </div>
                

                </div>

                <div class="droite col-12 col-md-5 d-flex flex-column justify-content-center align-items-center px-lg-5  image">

                    <div class="d-block d-md-none">
                        <img src="${url.resourcesPath}/images/logoUgbp.png" class="" alt=" logoUGB" srcset="">
                    </div>

                    <div class="custom-bar d-none d-md-block">
                        <img src="${url.resourcesPath}/images/bar.png" alt=" bar" srcset="">
                    </div>

                    <div class="d-flex d-md-none justify-content-center text-center mt-3">
                        <h3 class=" h1" style="color: #B25F35 !important; ">Espace d’authentification</h3>
                    </div>

                    <div class="d-none d-md-block" style = "height:5vh;">
                    </div>
                    <div class="" style="width:100%;"> 
                        <@layout.registrationLayout displayMessage=!messagesPerField.existsError('username','password') displayInfo=realm.password && realm.registrationAllowed && !registrationDisabled??; section>


                            <#if section = "header"> 
                            <span class="h3 text-white d-none d-md-block" >Vous allez vous connecter en tant que étudiant. </span>
                            <#elseif section = "form">
                                <div id="kc-form">
                                    <div id="kc-form-wrapper">
                                        <#if realm.password>
                                            <form id="kc-form-login" onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post">
                                                <#if !usernameHidden??>

                                                <!-- spacer -->
                                                <div style="height: 10vh;"></div>

                                                    <!-- email field -->

                                                    <div class="${properties.kcFormGroupClass!} mt-3">
                                                        <!-- for design no need email label -->
                                                        <!-- <label for="username" class="${properties.kcLabelClass!} form-label"><#if !realm.loginWithEmailAllowed>${msg("username")}<#elseif !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}<#else>${msg("email")}</#if></label>  -->
                                                        <div class="d-flex cutom-form">
                                                            <svg width="39" height="39" viewBox="0 0 39 39" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                            <path class="c-stroke" fill-rule="evenodd" clip-rule="evenodd" d="M8.59646 9.94922H29.6749C30.5585 9.94922 31.2749 10.6656 31.2749 11.5492C31.2749 11.551 31.2749 11.5528 31.2749 11.5546L31.2269 25.9207C31.224 26.8023 30.5085 27.5154 29.6269 27.5154H8.58047C7.69681 27.5154 6.98047 26.799 6.98047 25.9154C6.98047 25.9148 6.98047 25.9142 6.98047 25.9136L6.99646 11.5474C6.99744 10.6645 7.7135 9.94922 8.59646 9.94922Z" stroke="white" stroke-width="2.4" stroke-linejoin="round"/>
                                                            <path class="c-fill"   d="M18.4427 21.6694C18.9473 22.0991 19.7046 22.0385 20.1344 21.534C20.5641 21.0295 20.5035 20.2721 19.999 19.8423L18.4427 21.6694ZM8.20043 9.79225C7.69591 9.3625 6.93853 9.42311 6.50878 9.92763C6.07903 10.4321 6.13964 11.1895 6.64416 11.6193L8.20043 9.79225ZM31.7413 11.6174C32.2448 11.1864 32.3035 10.4289 31.8725 9.92539C31.4415 9.42193 30.684 9.36318 30.1806 9.79416L31.7413 11.6174ZM18.4405 19.8443C17.937 20.2752 17.8783 21.0328 18.3093 21.5362C18.7403 22.0397 19.4978 22.0984 20.0012 21.6675L18.4405 19.8443ZM19.999 19.8423C16.0661 16.4923 13.1165 13.9798 11.1501 12.3048C10.1669 11.4673 9.42945 10.8391 8.93784 10.4204C8.69204 10.211 8.50768 10.054 8.38478 9.94928C8.32334 9.89695 8.27723 9.85767 8.24652 9.83151C8.23113 9.81841 8.21966 9.80863 8.21195 9.80207C8.20807 9.79876 8.20528 9.79638 8.20331 9.79471C8.20229 9.79384 8.20169 9.79332 8.20115 9.79287C8.20079 9.79256 8.20084 9.79261 8.20061 9.79241C8.20055 9.79235 8.2005 9.79232 8.20047 9.79229C8.20044 9.79226 8.20043 9.79225 7.42229 10.7058C6.64416 11.6193 6.64417 11.6193 6.6442 11.6193C6.64423 11.6193 6.64428 11.6194 6.64434 11.6194C6.64434 11.6194 6.64475 11.6198 6.64488 11.6199C6.6453 11.6203 6.64614 11.621 6.64704 11.6217C6.64891 11.6233 6.65189 11.6259 6.65568 11.6291C6.66334 11.6356 6.67491 11.6455 6.69025 11.6585C6.72098 11.6847 6.76705 11.724 6.82851 11.7763C6.95142 11.881 7.13576 12.038 7.38157 12.2474C7.87318 12.6662 8.61059 13.2943 9.5938 14.1318C11.5602 15.8068 14.5099 18.3193 18.4427 21.6694L19.999 19.8423ZM30.9609 10.7058C30.1806 9.79416 30.1805 9.79418 30.1805 9.7942C30.1805 9.79423 30.1804 9.79427 30.1804 9.79432C30.1803 9.79436 30.18 9.79463 30.1798 9.79478C30.1793 9.79522 30.1787 9.79577 30.1777 9.79662C30.1758 9.79825 30.1729 9.80071 30.1691 9.80398C30.1615 9.81052 30.15 9.82034 30.1347 9.83342C30.1041 9.8596 30.0583 9.89885 29.9971 9.9512C29.8748 10.0559 29.6914 10.2129 29.4468 10.4223C28.9576 10.841 28.2239 11.4692 27.2455 12.3067C25.2889 13.9817 22.3538 16.4942 18.4405 19.8443L20.0012 21.6675C23.9146 18.3174 26.8496 15.8049 28.8063 14.1299C29.7846 13.2924 30.5184 12.6642 31.0076 12.2455C31.2521 12.0361 31.4356 11.8791 31.5579 11.7744C31.619 11.7221 31.6649 11.6828 31.6955 11.6566C31.7107 11.6435 31.7222 11.6337 31.7298 11.6272C31.7337 11.6239 31.7365 11.6215 31.7384 11.6198C31.7394 11.619 31.7402 11.6184 31.7406 11.618C31.7409 11.6177 31.7409 11.6177 31.7411 11.6175C31.7412 11.6175 31.7412 11.6174 31.7413 11.6174C31.7413 11.6174 31.7413 11.6174 30.9609 10.7058Z" fill="white"/>
                                                            </svg> 
                                                            <input tabindex="2" id="username" type="text" placeholder="zoulaykha@ugb.edu.sn"  class="${properties.kcInputClass!}  b-0 input-custom" name="username" value="${(login.username!'')}"  autofocus autocomplete="username"
                                                            aria-invalid="<#if messagesPerField.existsError('username','password')>true</#if>"
                                                            />

                                                        </div>

                                                    

                                                        <#if messagesPerField.existsError('username','password')>
                                                            <span id="input-error" class="${properties.kcInputErrorMessageClass!} "  aria-live="polite">
                                                                    ${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}
                                                            </span>
                                                        </#if>

                                                    </div>
                                                </#if>


                                                <!-- password field -->

                                                


                                                <div class="${properties.kcFormGroupClass!} mt-4">
                                                    <!-- for design no need password label -->
                                                    <!-- <label for="password " class="${properties.kcLabelClass!} form-label">${msg("password")}</label> -->

                                                    <div class="${properties.kcInputGroup!}">
                                                        <div class="d-flex cutom-form">
                                                            <svg width="39" height="39" viewBox="0 0 39 39" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                                <path fill-rule="evenodd" class="c-stroke" clip-rule="evenodd" d="M12.7992 15.5967H27.1992C28.0829 15.5967 28.7992 16.313 28.7992 17.1967V28.3967C28.7992 29.2803 28.0829 29.9967 27.1992 29.9967H12.7992C11.9156 29.9967 11.1992 29.2803 11.1992 28.3967V17.1967C11.1992 16.313 11.9156 15.5967 12.7992 15.5967Z" stroke="white" stroke-width="2.4" stroke-linejoin="round"/>
                                                                <path class="c-stroke"  d="M14.8672 15.5969V11.5337C14.8672 8.69866 17.1655 6.40039 20.0005 6.40039C22.8356 6.40039 25.1339 8.69866 25.1339 11.5337V11.5656V11.5656V15.5969" stroke="white" stroke-width="2.4" stroke-linecap="round" stroke-linejoin="round"/>
                                                            </svg>
                                                                <input tabindex="2" id="password" class="${properties.kcInputClass!} " name="password" type="password" autocomplete="current-password"
                                                                    aria-invalid="<#if messagesPerField.existsError('username','password')>true</#if>"
                                                                />
                                                            <!-- toggle password button -->
                                                            <button class="custom-icon-button me-1" id="toggle-password-custom"><i class="bi bi-eye"></i></button>
                                                        </div>

                                                        <!-- for enable or desable show password -->

                                                        <!-- for now unrequired, do above -->

                                                       <!-- <button class="${properties.kcFormPasswordVisibilityButtonClass!}" type="button" aria-label="${msg('showPassword')}"
                                                                aria-controls="password" data-password-toggle tabindex="4"
                                                                data-icon-show="${properties.kcFormPasswordVisibilityIconShow!}" data-icon-hide="${properties.kcFormPasswordVisibilityIconHide!}"
                                                                data-label-show="${msg('showPassword')}" data-label-hide="${msg('hidePassword')}">
                                                            <i class="${properties.kcFormPasswordVisibilityIconShow!}" aria-hidden="true"></i>
                                                        </button> -->
                                                    </div>

                                                    <#if usernameHidden?? && messagesPerField.existsError('username','password')>
                                                        <span id="input-error" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                                                                ${kcSanitize(messagesPerField.getFirstError('username','password'))?no_esc}
                                                        </span>
                                                    </#if>

                                                </div>

                                                <div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
                                                    <div id="kc-form-options">
                                                        <!-- for rememberMe not work -->
                                                        <#if realm.rememberMe && !usernameHidden??>
                                                            <!--  <div class="checkbox">
                                                                <label>
                                                                    <#if login.rememberMe??>
                                                                        <input tabindex="5" id="rememberMe" name="rememberMe" type="checkbox" checked> ${msg("rememberMe")}
                                                                    <#else>
                                                                        <input tabindex="5" id="rememberMe" name="rememberMe" type="checkbox"> ${msg("rememberMe")}
                                                                    </#if>
                                                                </label>
                                                            </div> -->
                                                        </#if>
                                                    </div>
                                                    <div class="${properties.kcFormOptionsWrapperClass!}">
                                                            <!-- for reset passwork not work used and not needed -->

                                                           <!--<#if realm.resetPasswordAllowed>
                                                                <span ><a  class="c-color" tabindex="6" href="${url.loginResetCredentialsUrl}">${msg("doForgotPassword")}</a></span>
                                                            </#if> -->
                                                    </div>

                                                </div>

                                                <div id="kc-form-buttons" class="${properties.kcFormGroupClass!} custom-button">
                                                    <input type="hidden" id="id-hidden-input" name="credentialId" <#if auth.selectedCredential?has_content> value="${auth.selectedCredential}"</#if> />
                                                    <!-- <input tabindex="7" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="submit" value="${msg('doLogIn')}"/> -->
                                                    
                                                    <input tabindex="7" class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="login" id="kc-login" type="submit" value="Connexion"/>
                                                </div>
                                            </form>


                                            <div class="d-flex justify-content-center flex-column align-items-start align-items-md-center mt-1 mt-md-5">
                                                <div class=" text-center text-dark text-lg-white c-color">
                                                    <!-- Si votre compte n’est pas activé ? <br> -->
                                                    <a data-bs-toggle="tooltip" data-bs-title="Le lorem ipsum est, en imprimerie, une suite de mots!" data-bs-placement="right" href="${url.loginResetCredentialsUrl}" class="c-color"> Mot de passe oublié?</a>
                                                    
                                                    
                                                </div>
                                                <!-- <div class="text-white text-center mt-5 c-color"> 
                                                    Avez-vous oublié votre mot de passe ? <br>
                                                    <a href="${url.loginResetCredentialsUrl}" style="color: white;"> Réinitialiser mot de passe </a>
                                                    
                                                    
                                                </div> -->
                                            </div>

                                        </#if>
                                    </div>
                                </div>
                                <script type="module" src="${url.resourcesPath}/js/passwordVisibility.js"></script>
                            <#elseif section = "info" >
                            <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
                                <div id="kc-registration-container">
                                    <div id="kc-registration">
                                        <span>${msg("noAccount")} <a tabindex="8"
                                                                    href="${url.registrationUrl}">${msg("doRegister")}</a></span>
                                    </div>
                                </div>
                            </#if>
                            <#elseif section = "socialProviders" >
                                <#if realm.password && social.providers??>
                                    <div id="kc-social-providers" class="${properties.kcFormSocialAccountSectionClass!}">
                                        <hr/>
                                        <h2>${msg("identity-provider-login-label")}</h2>

                                        <ul class="${properties.kcFormSocialAccountListClass!} <#if social.providers?size gt 3>${properties.kcFormSocialAccountListGridClass!}</#if>">
                                            <#list social.providers as p>
                                                <li>
                                                    <a id="social-${p.alias}" class="${properties.kcFormSocialAccountListButtonClass!} <#if social.providers?size gt 3>${properties.kcFormSocialAccountGridItem!}</#if>"
                                                            type="button" href="${p.loginUrl}">
                                                        <#if p.iconClasses?has_content>
                                                            <i class="${properties.kcCommonLogoIdP!} ${p.iconClasses!}" aria-hidden="true"></i>
                                                            <span class="${properties.kcFormSocialAccountNameClass!} kc-social-icon-text">${p.displayName!}</span>
                                                        <#else>
                                                            <span class="${properties.kcFormSocialAccountNameClass!}">${p.displayName!}</span>
                                                        </#if>
                                                    </a>
                                                </li>
                                            </#list>
                                        </ul>
                                    </div>
                                </#if>
                            </#if>

                        </@layout.registrationLayout>
                    </div>

                </div>

            </div>

</div>

<!-- hide show password -->
<script src="${url.resourcesPath}/js/togglepassword.js"></script>

<!-- bootstrap js -->
<script src="${url.resourcesPath}/js/bootstrap.bundle.js"></script>

<!--initialisation tooltip, required for work well -->
<script>
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
</script>








