import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {

  const authToken = '';


  // Clone the request and add the authorization header
  if (authToken.length > 0) {
    const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${authToken}`
    }
  });
    return next(authReq);
  }

  console.log('from interceptor');

  return next(req);
};
