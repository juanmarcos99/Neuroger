/*
 * histcounts.c
 *
 * Code generation for function 'histcounts'
 *
 */

/* Include files */
#include <math.h>
#include <string.h>
#include "rt_nonfinite.h"
#include "getglobalCDR.h"
#include "histcounts.h"

/* Function Definitions */
void histcounts(const double x_data[], const int x_size[2], const double
                varargin_1[6], double n[5], double bin_data[], int bin_size[2])
{
  int low_ip1;
  signed char unnamed_idx_1;
  int b_n[5];
  int b_bin_data[5];
  int nx;
  double leftEdge;
  double delta;
  int k;
  double bGuess;
  int low_i;
  boolean_T guard1 = false;
  int high_i;
  int mid_i;
  for (low_ip1 = 0; low_ip1 < 5; low_ip1++) {
    b_n[low_ip1] = 0;
  }

  unnamed_idx_1 = (signed char)x_size[1];
  if (0 <= unnamed_idx_1 - 1) {
    memset(&b_bin_data[0], 0, (unsigned int)(unnamed_idx_1 * (int)sizeof(int)));
  }

  nx = x_size[1];
  leftEdge = varargin_1[0];
  delta = varargin_1[1] - varargin_1[0];
  for (k = 0; k < nx; k++) {
    if ((x_data[k] >= leftEdge) && (x_data[k] <= varargin_1[5])) {
      bGuess = ceil((x_data[k] - leftEdge) / delta);
      guard1 = false;
      if ((bGuess >= 1.0) && (bGuess < 6.0)) {
        low_ip1 = (int)bGuess - 1;
        if ((x_data[k] >= varargin_1[low_ip1]) && (x_data[k] < varargin_1[(int)
             bGuess])) {
          b_n[low_ip1]++;
          b_bin_data[k] = (int)bGuess;
        } else {
          guard1 = true;
        }
      } else {
        guard1 = true;
      }

      if (guard1) {
        low_i = 1;
        low_ip1 = 2;
        high_i = 6;
        while (high_i > low_ip1) {
          mid_i = (low_i + high_i) >> 1;
          if (x_data[k] >= varargin_1[mid_i - 1]) {
            low_i = mid_i;
            low_ip1 = mid_i + 1;
          } else {
            high_i = mid_i;
          }
        }

        b_n[low_i - 1]++;
        b_bin_data[k] = low_i;
      }
    }
  }

  bin_size[0] = 1;
  bin_size[1] = unnamed_idx_1;
  low_i = unnamed_idx_1;
  for (low_ip1 = 0; low_ip1 < low_i; low_ip1++) {
    bin_data[low_ip1] = b_bin_data[low_ip1];
  }

  for (low_ip1 = 0; low_ip1 < 5; low_ip1++) {
    n[low_ip1] = b_n[low_ip1];
  }
}

/* End of code generation (histcounts.c) */
