import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);

  return next(req).pipe(
    catchError((error) => {
      let message = 'Ocurrió un error inesperado';
      let code = 'UNEXPECTED_ERROR';

      if (error.error && typeof error.error === 'object') {
        // Nuestro GenericResponse de error
        message = error.error.message || message;
        code = error.error.code || code;
      } else if (error.error && typeof error.error === 'string') {
        message = error.error;
      } else if (error.status === 0) {
        message = 'No se pudo conectar al servidor. Verifica tu conexión.';
      } else if (error.status === 401) {
        message = 'Sesión expirada. Por favor inicia sesión nuevamente.';
      } else if (error.status === 403) {
        message = 'No tienes permisos para realizar esta acción.';
      } else if (error.status === 500) {
        message = 'Error interno del servidor. Intenta más tarde.';
      }

      // Muestra snackbar con el mensaje real del backend
      snackBar.open(message, 'Cerrar', {
        duration: 6000,
        panelClass: ['error-snackbar'],
        horizontalPosition: 'center',
        verticalPosition: 'top'
      });

      // Re-lanza el error para que el componente lo maneje si quiere
      return throwError(() => ({ message, code, status: error.status }));
    })
  );
};