/*
 * getglobalCDR.h
 *
 * Code generation for function 'getglobalCDR'
 *
 */

#ifndef GETGLOBALCDR_H
#define GETGLOBALCDR_H

/* Include files */
#include <stddef.h>
#include <stdlib.h>
#include "rtwtypes.h"
#include "getglobalCDR_types.h"

/* Function Declarations */
#ifdef __cplusplus

extern "C" {

#endif

  extern void getglobalCDR(const double boxscores[6], double CDR_data[], int
    CDR_size[2], double *CDR_SB, boolean_T rules[6], boolean_T excep[3]);

#ifdef __cplusplus

}
#endif
#endif

/* End of code generation (getglobalCDR.h) */
