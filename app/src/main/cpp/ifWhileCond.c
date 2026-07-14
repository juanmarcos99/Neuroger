/*
 * ifWhileCond.c
 *
 * Code generation for function 'ifWhileCond'
 *
 */

/* Include files */
#include "rt_nonfinite.h"
#include "getglobalCDR.h"
#include "ifWhileCond.h"

/* Function Definitions */
boolean_T ifWhileCond(const boolean_T x_data[], const int x_size[2])
{
  boolean_T y;
  int k;
  boolean_T exitg1;
  y = (x_size[1] != 0);
  if (y) {
    k = 0;
    exitg1 = false;
    while ((!exitg1) && (k <= x_size[1] - 1)) {
      if (!x_data[k]) {
        y = false;
        exitg1 = true;
      } else {
        k++;
      }
    }
  }

  return y;
}

/* End of code generation (ifWhileCond.c) */
