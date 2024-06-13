<#import "template.ftl" as layout>
<link rel="stylesheet" href="${url.resourcesPath}/css/login-reset-password.css">
<link rel="stylesheet" href="${url.resourcesPath}/css/bootstrap.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.5.0/font/bootstrap-icons.min.css">

<div class="container-fluid"> 

    <div class="row" style="height: 100vh;">
        <div class="gauche col-12 col-md-7 d-flex flex-column justify-content-center align-items-center d-none  d-md-flex "  style="background-image: url('${url.resourcesPath}/images/DesktopBG.png'); background-size: cover;" >


                    <div class="d-flex justify-content-center" >
                        <img src="${url.resourcesPath}/images/logoUgb.png" class="mt-4" alt="icon-reset-password" srcset="">
                    </div>
                     <div class="">
                            <h1 class="text-center text-white size-title" style=" color: #B25F35 !important ; ">mot de passe oublié</h1>
                    </div>

        </div>

        <div class="droite col-12 col-md-5 d-flex flex-column justify-content-center align-items-center px-lg-5  image " > 

                
                    <div class="d-block d-md-none">
                        <img src="${url.resourcesPath}/images/logoUgbp.png" class="" alt=" logoUGB" srcset="">
                    </div>

                    <!-- <div class="custom-bar d-none d-md-block" style="width:100%;">
                        <img src="${url.resourcesPath}/images/bar.png" alt=" bar" srcset="">
                    </div> -->

                   

                    <div class="d-flex d-md-none justify-content-center text-center mt-3">
                        <h3 class="h1 c-color">Mot de passe oublié</h3>
                    </div>

                    <div class="d-none d-md-block" style = "height:5vh;">
                    </div>

                <div class="text-center" style="width:100%; ">

                        <@layout.registrationLayout displayInfo=true displayMessage=!messagesPerField.existsError('username'); section>
                        <#if section = "header">
                            <!-- ${msg("emailForgotTitle")}  Test à modifier 
                            <span class="text-center h3">
                                Mot de passe oublié
                            </span> -->
                            <div class="mt-3 mb-5 c-color" style=" font-size: 14px; color:#272727; font-weight: normal;">
                            Enter your username or email address and we will send you instructions on how to create a new password.
                            </div>
                        <#elseif section = "form">
                            <form id="kc-reset-password-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
                                <div class="${properties.kcFormGroupClass!}">
                                    <div class="${properties.kcLabelWrapperClass!}">
                                        <!-- <label for="username" class="${properties.kcLabelClass!} text-start"><#if !realm.loginWithEmailAllowed>${msg("username")}<#elseif !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}<#else>${msg("email")}</#if></label> -->
                                    </div>
                                    <div class="${properties.kcInputWrapperClass!}">
                                        <div class="d-flex cutom-form"> 
                                        <svg width="45" height="44" viewBox="0 0 45 44" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path class="c-stroke" fill-rule="evenodd" clip-rule="evenodd" d="M10.0113 11.3838H34.5641C35.5935 11.3838 36.4279 12.2034 36.4279 13.2144C36.4279 13.2164 36.4279 13.2185 36.4279 13.2205L36.372 29.657C36.3686 30.6657 35.5352 31.4815 34.5083 31.4815H9.99264C8.96333 31.4815 8.12891 30.6619 8.12891 29.6509C8.12891 29.6503 8.12891 29.6496 8.12891 29.6489L8.14753 13.2123C8.14868 12.2021 8.98276 11.3838 10.0113 11.3838Z" stroke="#494646" stroke-width="2.4" stroke-linejoin="round"/>
                                        <path class="c-fill" d="M21.6167 24.6674C22.125 25.0927 22.8818 25.0254 23.3071 24.5171C23.7324 24.0088 23.665 23.252 23.1567 22.8267L21.6167 24.6674ZM9.41335 11.3282C8.90505 10.903 8.14824 10.9703 7.72296 11.4786C7.29769 11.9869 7.365 12.7437 7.8733 13.169L9.41335 11.3282ZM36.8348 13.1671C37.342 12.7405 37.4075 11.9836 36.981 11.4763C36.5545 10.9691 35.7975 10.9036 35.2902 11.3301L36.8348 13.1671ZM21.615 22.8286C21.1077 23.2551 21.0423 24.0121 21.4688 24.5193C21.8953 25.0266 22.6523 25.0921 23.1595 24.6655L21.615 22.8286ZM23.1567 22.8267C18.5756 18.9939 15.1398 16.1193 12.8492 14.2028C11.7039 13.2446 10.845 12.526 10.2723 12.0469C9.98599 11.8073 9.77125 11.6277 9.62809 11.5079C9.5565 11.448 9.50283 11.4031 9.46703 11.3731C9.44916 11.3582 9.43569 11.3469 9.42677 11.3395C9.42235 11.3358 9.41889 11.3329 9.4167 11.331C9.41552 11.33 9.41481 11.3295 9.41419 11.3289C9.41397 11.3287 9.41364 11.3285 9.41356 11.3284C9.41349 11.3283 9.41344 11.3283 9.4134 11.3283C9.41337 11.3282 9.41335 11.3282 8.64332 12.2486C7.8733 13.169 7.87332 13.169 7.87335 13.169C7.87339 13.169 7.87344 13.1691 7.87351 13.1691C7.87403 13.1696 7.87348 13.1691 7.87414 13.1697C7.8747 13.1701 7.87554 13.1708 7.87666 13.1718C7.8789 13.1736 7.88224 13.1764 7.88672 13.1802C7.89569 13.1877 7.90907 13.1989 7.92698 13.2139C7.96278 13.2438 8.01646 13.2887 8.08804 13.3486C8.2312 13.4684 8.44594 13.6481 8.73226 13.8876C9.3049 14.3667 10.1639 15.0854 11.3091 16.0436C13.5997 17.96 17.0356 20.8346 21.6167 24.6674L23.1567 22.8267ZM36.0625 12.2486C35.2902 11.3301 35.2902 11.3301 35.2902 11.3302C35.2901 11.3302 35.2901 11.3302 35.29 11.3303C35.2898 11.3305 35.2898 11.3305 35.2894 11.3308C35.2889 11.3312 35.288 11.332 35.2869 11.3329C35.2846 11.3348 35.2813 11.3376 35.2769 11.3413C35.268 11.3488 35.2546 11.3601 35.2368 11.375C35.2012 11.405 35.1478 11.4499 35.0765 11.5098C34.9341 11.6296 34.7204 11.8092 34.4355 12.0488C33.8657 12.5279 33.011 13.2465 31.8714 14.2047C29.5922 16.1212 26.1734 18.9958 21.615 22.8286L23.1595 24.6655C27.7179 20.8327 31.1368 17.9581 33.416 16.0417C34.5556 15.0835 35.4103 14.3648 35.9801 13.8857C36.265 13.6462 36.4787 13.4665 36.6211 13.3467C36.6923 13.2868 36.7457 13.2419 36.7814 13.212C36.7992 13.197 36.8125 13.1858 36.8214 13.1783C36.8258 13.1746 36.8292 13.1717 36.8314 13.1699C36.8326 13.1689 36.8334 13.1683 36.8339 13.1678C36.8342 13.1676 36.8345 13.1673 36.8346 13.1672C36.8346 13.1672 36.8347 13.1671 36.8347 13.1671C36.8348 13.1671 36.8348 13.1671 36.0625 12.2486Z" fill="#494646"/>
                                        </svg>


                                            <input type="text" placeholder="@ugb.edu.sn" id="username" name="username" class="${properties.kcInputClass!} b-0 input-custom" autofocus value="${(auth.attemptedUsername!'')}" aria-invalid="<#if messagesPerField.existsError('username')>true</#if>"/>
                                        </div>
                                        <#if messagesPerField.existsError('username')>
                                            <span id="input-error-username" class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                                                        ${kcSanitize(messagesPerField.get('username'))?no_esc}
                                            </span>
                                        </#if>
                                        
                                    </div>
                                </div>
                                

                                <div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
                                    <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                                        <div class="${properties.kcFormOptionsWrapperClass!}">
                                            <span><a href="${url.loginUrl}">${kcSanitize(msg("backToLogin"))?no_esc}</a></span>
                                        </div>
                                    </div>

                                    

                                    <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!} custom-button">
                                        <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!} " type="submit" value="Envoyer"/>
                                        <!-- <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!} " type="submit" value="${msg("doSubmit")}"/> -->
                                    </div>
                                </div>
                            </form>

                            <!--custom back to login side -->
                            <div class="c-color d-flex flex-start align-items-center">
                                <i class="bi bi-arrow-left-short mt-1" style="font-size:1.5rem;"></i> 
                                <span>
                                    <a href="${url.loginUrl}" class="c-color">retour à la page login</a>
                                </span>

                            </div>
                        <#elseif section = "info" >
                            <#if realm.duplicateEmailsAllowed>
                               <!-- ${msg("emailInstructionUsername")} -->
                            <#else>
                               <!-- ${msg("emailInstruction")} -->
                            </#if>
                        </#if> 
                    </@layout.registrationLayout>
                
                
                </div>

        
            
        
        </div>

    </div>

</div>


