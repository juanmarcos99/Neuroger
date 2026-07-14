/*
 * sum.c
 *
 * Code generation for function 'sum'
 *
 */

/* Include files */
#include "rt_nonfinite.h"
#include "getglobalCDR.h"
#include "sum.h"

/* Function Definitions */
double b_sum(const double x[6])
{
  double y;
  int k;
  y = x[0];
  for (k = 0; k < 5; k++) {
    y += x[k + 1];
  }

  return y;
}

double sum(const boolean_T x[5])
{
  return ((((double)x[0] + (double)x[1]) + (double)x[2]) + (double)x[3]) +
    (double)x[4];
}

/* End of code generation (sum.c) */
