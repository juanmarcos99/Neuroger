/*
 * sortIdx.h
 *
 * Code generation for function 'sortIdx'
 *
 */

#ifndef SORTIDX_H
#define SORTIDX_H

/* Include files */
#include <stddef.h>
#include <stdlib.h>
#include "rtwtypes.h"
#include "getglobalCDR_types.h"

/* Function Declarations */
#ifdef __cplusplus

extern "C" {

#endif

  extern void merge(int idx[6], double x[6], int offset, int np, int nq, int
                    iwork[6], double xwork[6]);

#ifdef __cplusplus

}
#endif
#endif

/* End of code generation (sortIdx.h) */
