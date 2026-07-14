/*
 * sortIdx.c
 *
 * Code generation for function 'sortIdx'
 *
 */

/* Include files */
#include "rt_nonfinite.h"
#include "getglobalCDR.h"
#include "sortIdx.h"

/* Function Definitions */
void merge(int idx[6], double x[6], int offset, int np, int nq, int iwork[6],
           double xwork[6])
{
  int n_tmp;
  int iout;
  int p;
  int i2;
  int q;
  int exitg1;
  if (nq != 0) {
    n_tmp = np + nq;
    for (iout = 0; iout < n_tmp; iout++) {
      i2 = offset + iout;
      iwork[iout] = idx[i2];
      xwork[iout] = x[i2];
    }

    p = 0;
    q = np;
    iout = offset - 1;
    do {
      exitg1 = 0;
      iout++;
      if (xwork[p] <= xwork[q]) {
        idx[iout] = iwork[p];
        x[iout] = xwork[p];
        if (p + 1 < np) {
          p++;
        } else {
          exitg1 = 1;
        }
      } else {
        idx[iout] = iwork[q];
        x[iout] = xwork[q];
        if (q + 1 < n_tmp) {
          q++;
        } else {
          q = iout - p;
          for (iout = p + 1; iout <= np; iout++) {
            i2 = q + iout;
            idx[i2] = iwork[iout - 1];
            x[i2] = xwork[iout - 1];
          }

          exitg1 = 1;
        }
      }
    } while (exitg1 == 0);
  }
}

/* End of code generation (sortIdx.c) */
