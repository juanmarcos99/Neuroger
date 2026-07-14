/*
 * sort1.c
 *
 * Code generation for function 'sort1'
 *
 */

/* Include files */
#include "rt_nonfinite.h"
#include "getglobalCDR.h"
#include "sort1.h"
#include "sortIdx.h"

/* Function Definitions */
void sort(double x[6])
{
  int i;
  double x4[4];
  int idx[6];
  signed char idx4[4];
  int nNaNs;
  double xwork[6];
  int k;
  signed char perm[4];
  int i3;
  int quartetOffset;
  int i2;
  int i4;
  int iwork[6];
  double d0;
  double d1;
  for (i = 0; i < 6; i++) {
    idx[i] = 0;
  }

  x4[0] = 0.0;
  idx4[0] = 0;
  x4[1] = 0.0;
  idx4[1] = 0;
  x4[2] = 0.0;
  idx4[2] = 0;
  x4[3] = 0.0;
  idx4[3] = 0;
  for (i = 0; i < 6; i++) {
    xwork[i] = 0.0;
  }

  nNaNs = 0;
  i = 0;
  for (k = 0; k < 6; k++) {
    if (rtIsNaN(x[k])) {
      idx[5 - nNaNs] = k + 1;
      xwork[5 - nNaNs] = x[k];
      nNaNs++;
    } else {
      i++;
      idx4[i - 1] = (signed char)(k + 1);
      x4[i - 1] = x[k];
      if (i == 4) {
        quartetOffset = k - nNaNs;
        if (x4[0] <= x4[1]) {
          i = 1;
          i2 = 2;
        } else {
          i = 2;
          i2 = 1;
        }

        if (x4[2] <= x4[3]) {
          i3 = 3;
          i4 = 4;
        } else {
          i3 = 4;
          i4 = 3;
        }

        d0 = x4[i - 1];
        d1 = x4[i3 - 1];
        if (d0 <= d1) {
          if (x4[i2 - 1] <= d1) {
            perm[0] = (signed char)i;
            perm[1] = (signed char)i2;
            perm[2] = (signed char)i3;
            perm[3] = (signed char)i4;
          } else if (x4[i2 - 1] <= x4[i4 - 1]) {
            perm[0] = (signed char)i;
            perm[1] = (signed char)i3;
            perm[2] = (signed char)i2;
            perm[3] = (signed char)i4;
          } else {
            perm[0] = (signed char)i;
            perm[1] = (signed char)i3;
            perm[2] = (signed char)i4;
            perm[3] = (signed char)i2;
          }
        } else {
          d1 = x4[i4 - 1];
          if (d0 <= d1) {
            if (x4[i2 - 1] <= d1) {
              perm[0] = (signed char)i3;
              perm[1] = (signed char)i;
              perm[2] = (signed char)i2;
              perm[3] = (signed char)i4;
            } else {
              perm[0] = (signed char)i3;
              perm[1] = (signed char)i;
              perm[2] = (signed char)i4;
              perm[3] = (signed char)i2;
            }
          } else {
            perm[0] = (signed char)i3;
            perm[1] = (signed char)i4;
            perm[2] = (signed char)i;
            perm[3] = (signed char)i2;
          }
        }

        i3 = perm[0] - 1;
        idx[quartetOffset - 3] = idx4[i3];
        i4 = perm[1] - 1;
        idx[quartetOffset - 2] = idx4[i4];
        i = perm[2] - 1;
        idx[quartetOffset - 1] = idx4[i];
        i2 = perm[3] - 1;
        idx[quartetOffset] = idx4[i2];
        x[quartetOffset - 3] = x4[i3];
        x[quartetOffset - 2] = x4[i4];
        x[quartetOffset - 1] = x4[i];
        x[quartetOffset] = x4[i2];
        i = 0;
      }
    }
  }

  if (i > 0) {
    perm[1] = 0;
    perm[2] = 0;
    perm[3] = 0;
    if (i == 1) {
      perm[0] = 1;
    } else if (i == 2) {
      if (x4[0] <= x4[1]) {
        perm[0] = 1;
        perm[1] = 2;
      } else {
        perm[0] = 2;
        perm[1] = 1;
      }
    } else if (x4[0] <= x4[1]) {
      if (x4[1] <= x4[2]) {
        perm[0] = 1;
        perm[1] = 2;
        perm[2] = 3;
      } else if (x4[0] <= x4[2]) {
        perm[0] = 1;
        perm[1] = 3;
        perm[2] = 2;
      } else {
        perm[0] = 3;
        perm[1] = 1;
        perm[2] = 2;
      }
    } else if (x4[0] <= x4[2]) {
      perm[0] = 2;
      perm[1] = 1;
      perm[2] = 3;
    } else if (x4[1] <= x4[2]) {
      perm[0] = 2;
      perm[1] = 3;
      perm[2] = 1;
    } else {
      perm[0] = 3;
      perm[1] = 2;
      perm[2] = 1;
    }

    for (k = 0; k < i; k++) {
      i3 = perm[k] - 1;
      i4 = ((k - nNaNs) - i) + 6;
      idx[i4] = idx4[i3];
      x[i4] = x4[i3];
    }
  }

  i = (nNaNs >> 1) + 6;
  for (k = 0; k <= i - 7; k++) {
    i3 = (k - nNaNs) + 6;
    i2 = idx[i3];
    idx[i3] = idx[5 - k];
    idx[5 - k] = i2;
    x[i3] = xwork[5 - k];
    x[5 - k] = xwork[i3];
  }

  if ((nNaNs & 1) != 0) {
    i -= nNaNs;
    x[i] = xwork[i];
  }

  if (6 - nNaNs > 1) {
    for (i = 0; i < 6; i++) {
      iwork[i] = 0;
    }

    i4 = (6 - nNaNs) >> 2;
    i2 = 4;
    while (i4 > 1) {
      if ((i4 & 1) != 0) {
        i4--;
        i = i2 * i4;
        i3 = 6 - (nNaNs + i);
        if (i3 > i2) {
          merge(idx, x, i, i2, i3 - i2, iwork, xwork);
        }
      }

      i = i2 << 1;
      i4 >>= 1;
      for (k = 0; k < i4; k++) {
        merge(idx, x, k * i, i2, i2, iwork, xwork);
      }

      i2 = i;
    }

    if (6 - nNaNs > i2) {
      merge(idx, x, 0, i2, 6 - (nNaNs + i2), iwork, xwork);
    }
  }
}

/* End of code generation (sort1.c) */
