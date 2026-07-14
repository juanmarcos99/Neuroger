/*
 * getglobalCDR.c
 *
 * Code generation for function 'getglobalCDR'
 *
 */

/* Include files */
#include <math.h>
#include <string.h>
#include "rt_nonfinite.h"
#include "getglobalCDR.h"
#include "ifWhileCond.h"
#include "histcounts.h"
#include "sort1.h"
#include "sign.h"
#include "sum.h"
#include "getglobalCDR_initialize.h"
#include "getglobalCDR_terminate.h"

/* Function Definitions */
void getglobalCDR(const double boxscores[6], double CDR_data[], int CDR_size[2],
                  double *CDR_SB, boolean_T rules[6], boolean_T excep[3])
{
  int i0;
  double neq;
  boolean_T b_boxscores[5];
  double nhi;
  double nlo;
  boolean_T r1;
  boolean_T r2;
  double absxk;
  double x;
  double N[5];
  int trueCount;
  int i;
  int pEnd;
  boolean_T r6;
  boolean_T r3;
  double secmj_data[5];
  boolean_T r4;
  double unusedU0[6];
  static const double b[5] = { 0.1, 0.6, 1.1, 2.1, 3.1 };

  int a_size[2];
  boolean_T r5;
  boolean_T b_x[2];
  double a_data[5];
  double bin_data[5];
  int bin_size[2];
  int k;
  int b_CDR_size[2];
  boolean_T exitg1;
  int p;
  int i2;
  int c_CDR_size[2];
  int idx_data[5];
  signed char tmp_data[5];
  int q;
  int j;
  int qEnd;
  int kEnd;
  int exitg2;
  int iwork_data[5];
  int i1;
  int exponent;
  boolean_T x_data[5];

  /*  Function for computing Global CDR Score by applying CDR scoring rules. */
  /*  Input: */
  /*  boxscores -> 6-element vector with the evaluation scores for each domain: */
  /*               first element corresponds to Memory domain, which is the */
  /*               primary category. The other 5 elements correspond to the */
  /*               secondary categories in this order:  */
  /*                Orientation */
  /*                Judgement and problem-solving */
  /*                Community affairs */
  /*                Home and hobbies */
  /*                Personal Care */
  /*               Each score must be one of these five numbers: 0, 0.5, 1, 2, 3, */
  /*               ??? except for the Personal Care score, which cannot have 0.5. */
  /*  */
  /*  Output: */
  /*  CDR       -> Global CDR score (scalar that takes values of 0, 0.5, 1, 2, 3) */
  /*                If CDR = -1, this means that no rule or exception was met. */
  /*  CDR_SB    -> CDR sum of boxes. Scalar with the sum of the scores enter in */
  /*                boxscores. This is used in some studies. */
  /*  rules     -> 6-element logical vector representing the rules that were */
  /*               met (1 or True) or not (0 or False)    */
  /*               If more than one rule was true, the latest defined the final CDR. */
  /*  excep     -> 3-element logical vector representing the exceptions that were */
  /*               met (1 or True) or not (0 or False)    */
  /*               If more than one exception was true, the latest defined the final CDR. */
  /*  */
  /*  Rules obtained from: */
  /*  Morris, J.C. (1993). The clinical dementia rating (CDR): Current version and scoring rules. Neurology, */
  /*  43(11), 2412-2414. */
  /*  */
  /*  Author: Eduardo MM, Karen AM, Feb 2023. */
  /*  Feb. 28. EMM added the CDR_SB output.  */
  /*  Check input consistency */
  /*  possible values */
  /* if boxscores(end)==0.5;  */
  /*     boxscores(end) = 0; % setting last item to zero. */
  /*     warning('The last item should not be 0.5, we changed it to 0...');  */
  /* end */
  /*  Miscelanea */
  CDR_size[0] = 1;
  CDR_size[1] = 1;
  CDR_data[0] = -1.0;

  /*  This value is to show that no rule or exception were met. */
  /*  Memory score (M, primary category) */
  /*  scores for secondary categories */
  for (i0 = 0; i0 < 5; i0++) {
    b_boxscores[i0] = (boxscores[1 + i0] == boxscores[0]);
  }

  neq = sum(b_boxscores);

  /*  No. of sec scores equal to memory */
  for (i0 = 0; i0 < 5; i0++) {
    b_boxscores[i0] = (boxscores[1 + i0] > boxscores[0]);
  }

  nhi = sum(b_boxscores);

  /*  No. of sec scores higher than memory */
  for (i0 = 0; i0 < 5; i0++) {
    b_boxscores[i0] = (boxscores[1 + i0] < boxscores[0]);
  }

  nlo = sum(b_boxscores);

  /*  No. of sec scores lower than memory */
  /*  Compute simple sum of boxes */
  *CDR_SB = b_sum(boxscores);

  /*  Check rules and compute global score (using priority of rules) */
  /*  Rule 1: IF at least 3 secondary categories are equal to M */
  r1 = (neq >= 3.0);

  /*  THEN CDR = M */
  if (r1) {
    CDR_size[0] = 1;
    CDR_size[1] = 1;
    CDR_data[0] = boxscores[0];
  }

  /*  Rule 2: Whenever three or more secondary categories are higher or lower than M,    */
  if ((nhi >= 3.0) || (nlo >= 3.0)) {
    r2 = true;
  } else {
    r2 = false;
  }

  /*  THEN: CDR = score of majority of secondary categories on whichever  */
  /*  side of M has the greater number of secondary categories.  */
  /*  (EMM: choose value closest to M if there are ties (exception 1 below)) */
  if (r2) {
    absxk = nhi - nlo;
    x = absxk;
    b_sign(&x);
    trueCount = 0;
    for (i = 0; i < 5; i++) {
      r6 = (x * (boxscores[1 + i] - boxscores[0]) > 0.0);
      b_boxscores[i] = r6;
      if (r6) {
        trueCount++;
      }
    }

    pEnd = 0;
    for (i = 0; i < 5; i++) {
      if (b_boxscores[i]) {
        secmj_data[pEnd] = boxscores[1 + i];
        pEnd++;
      }
    }

    /*  hi/lo majority values in sec cat */
    /*  small value for correct edges in histogram */
    /* histogram(sign(nhi-nlo)*secmj,sort([0 sign(nhi-nlo)*(posc+delta)])); % optional. comment */
    x = absxk;
    b_sign(&x);
    b_sign(&absxk);
    unusedU0[0] = 0.0;
    for (i0 = 0; i0 < 5; i0++) {
      unusedU0[i0 + 1] = absxk * b[i0];
    }

    sort(unusedU0);
    a_size[0] = 1;
    a_size[1] = trueCount;
    for (i0 = 0; i0 < trueCount; i0++) {
      a_data[i0] = x * secmj_data[i0];
    }

    histcounts(a_data, a_size, unusedU0, N, bin_data, bin_size);

    /*  count bin with highest number of values */
    if (!rtIsNaN(N[0])) {
      p = 1;
    } else {
      p = 0;
      k = 2;
      exitg1 = false;
      while ((!exitg1) && (k < 6)) {
        if (!rtIsNaN(N[k - 1])) {
          p = k;
          exitg1 = true;
        } else {
          k++;
        }
      }
    }

    if (p == 0) {
      p = 1;
    } else {
      absxk = N[p - 1];
      i0 = p + 1;
      for (k = i0; k < 6; k++) {
        x = N[k - 1];
        if (absxk < x) {
          absxk = x;
          p = k;
        }
      }
    }

    /*  select bin with highest number... */
    i2 = bin_size[1] - 1;
    trueCount = 0;
    for (i = 0; i <= i2; i++) {
      if (bin_data[i] == p) {
        trueCount++;
      }
    }

    pEnd = 0;
    for (i = 0; i <= i2; i++) {
      if (bin_data[i] == p) {
        tmp_data[pEnd] = (signed char)(i + 1);
        pEnd++;
      }
    }

    if (0 <= trueCount - 1) {
      memset(&idx_data[0], 0, (unsigned int)(trueCount * (int)sizeof(int)));
    }

    if (trueCount != 0) {
      i0 = trueCount - 1;
      for (k = 1; k <= i0; k += 2) {
        x = secmj_data[tmp_data[k] - 1];
        if ((secmj_data[tmp_data[k - 1] - 1] <= x) || rtIsNaN(x)) {
          idx_data[k - 1] = k;
          idx_data[k] = k + 1;
        } else {
          idx_data[k - 1] = k + 1;
          idx_data[k] = k;
        }
      }

      if ((trueCount & 1) != 0) {
        idx_data[trueCount - 1] = trueCount;
      }

      i = 2;
      while (i < trueCount) {
        i2 = i << 1;
        j = 1;
        for (pEnd = 1 + i; pEnd < trueCount + 1; pEnd = qEnd + i) {
          p = j;
          q = pEnd;
          qEnd = j + i2;
          if (qEnd > trueCount + 1) {
            qEnd = trueCount + 1;
          }

          k = 0;
          kEnd = qEnd - j;
          while (k + 1 <= kEnd) {
            i0 = idx_data[q - 1];
            x = secmj_data[tmp_data[i0 - 1] - 1];
            i1 = idx_data[p - 1];
            if ((secmj_data[tmp_data[i1 - 1] - 1] <= x) || rtIsNaN(x)) {
              iwork_data[k] = i1;
              p++;
              if (p == pEnd) {
                while (q < qEnd) {
                  k++;
                  iwork_data[k] = idx_data[q - 1];
                  q++;
                }
              }
            } else {
              iwork_data[k] = i0;
              q++;
              if (q == qEnd) {
                while (p < pEnd) {
                  k++;
                  iwork_data[k] = idx_data[p - 1];
                  p++;
                }
              }
            }

            k++;
          }

          for (k = 0; k < kEnd; k++) {
            idx_data[(j + k) - 1] = iwork_data[k];
          }

          j = qEnd;
        }

        i = i2;
      }
    }

    CDR_size[0] = 1;
    for (k = 0; k < trueCount; k++) {
      CDR_data[k] = secmj_data[tmp_data[idx_data[k] - 1] - 1];
    }

    k = 0;
    while ((k + 1 <= trueCount) && rtIsInf(CDR_data[k]) && (CDR_data[k] < 0.0))
    {
      k++;
    }

    q = k;
    k = trueCount;
    while ((k >= 1) && rtIsNaN(CDR_data[k - 1])) {
      k--;
    }

    qEnd = trueCount - k;
    exitg1 = false;
    while ((!exitg1) && (k >= 1)) {
      x = CDR_data[k - 1];
      if (rtIsInf(x) && (x > 0.0)) {
        k--;
      } else {
        exitg1 = true;
      }
    }

    i2 = (trueCount - k) - qEnd;
    p = -1;
    if (q > 0) {
      p = 0;
    }

    pEnd = (q + k) - q;
    while (q + 1 <= pEnd) {
      x = CDR_data[q];
      do {
        exitg2 = 0;
        q++;
        if (q + 1 > pEnd) {
          exitg2 = 1;
        } else {
          absxk = fabs(x / 2.0);
          if ((!rtIsInf(absxk)) && (!rtIsNaN(absxk))) {
            if (absxk <= 2.2250738585072014E-308) {
              absxk = 4.94065645841247E-324;
            } else {
              frexp(absxk, &exponent);
              absxk = ldexp(1.0, exponent - 53);
            }
          } else {
            absxk = rtNaN;
          }

          if ((fabs(x - CDR_data[q]) < absxk) || (rtIsInf(CDR_data[q]) &&
               rtIsInf(x) && ((CDR_data[q] > 0.0) == (x > 0.0)))) {
          } else {
            exitg2 = 1;
          }
        }
      } while (exitg2 == 0);

      p++;
      CDR_data[p] = x;
    }

    if (i2 > 0) {
      p++;
      CDR_data[p] = CDR_data[pEnd];
    }

    q = pEnd + i2;
    for (j = 0; j < qEnd; j++) {
      p++;
      CDR_data[p] = CDR_data[q + j];
    }

    if (1 > p + 1) {
      CDR_size[1] = 0;
    } else {
      CDR_size[1] = p + 1;
    }

    /*  get the score of sec cat with highest number... */
  } else {
    for (i0 = 0; i0 < 5; i0++) {
      N[i0] = 1.0 + (double)i0;
    }

    /*  this is for compatibility with C++ */
  }

  /*  Rule 3: When three secondary categories are scored on one side of M and */
  /*  two secondary categories are scored on the other side of M  */
  if (((nhi == 3.0) && (nlo == 2.0)) || ((nhi == 2.0) && (nlo == 3.0))) {
    r3 = true;
  } else {
    r3 = false;
  }

  /*  THEN CDR = M */
  if (r3) {
    CDR_size[0] = 1;
    CDR_size[1] = 1;
    CDR_data[0] = boxscores[0];
  }

  /*  Rule 4: When M = 0.5, IF at least three of the other categories are */
  /*  scored one or greater.  */
  if ((boxscores[0] == 0.5) && (nhi >= 3.0)) {
    r4 = true;
  } else {
    r4 = false;
  }

  /*  THEN CDR = 1 */
  if (r4) {
    CDR_size[0] = 1;
    CDR_size[1] = 1;
    CDR_data[0] = 1.0;
  }

  /*  Rule 5: IF M = 0.5, CDR cannot be 0; it can only be 0.5 or 1. */
  if (boxscores[0] == 0.5) {
    b_x[0] = (CDR_data[0] != 0.5);
    b_x[1] = (CDR_data[1] != 1.0);
    r6 = true;
    k = 0;
    exitg1 = false;
    while ((!exitg1) && (k < 2)) {
      if (!b_x[k]) {
        r6 = false;
        exitg1 = true;
      } else {
        k++;
      }
    }

    if (r6) {
      r5 = true;
    } else {
      r5 = false;
    }
  } else {
    r5 = false;
  }

  /*  THEN: (EMM: choose the value closest to current CDR)  */
  if (r5) {
    b_CDR_size[0] = 1;
    b_CDR_size[1] = CDR_size[1];
    pEnd = CDR_size[1];
    for (i0 = 0; i0 < pEnd; i0++) {
      b_boxscores[i0] = (CDR_data[i0] == 0.0);
    }

    if (ifWhileCond(b_boxscores, b_CDR_size)) {
      CDR_size[0] = 1;
      CDR_size[1] = 1;
      CDR_data[0] = 0.5;
    }

    c_CDR_size[0] = 1;
    c_CDR_size[1] = CDR_size[1];
    pEnd = CDR_size[1];
    for (i0 = 0; i0 < pEnd; i0++) {
      b_boxscores[i0] = (CDR_data[i0] > 1.0);
    }

    if (ifWhileCond(b_boxscores, c_CDR_size)) {
      CDR_size[0] = 1;
      CDR_size[1] = 1;
      CDR_data[0] = 1.0;
    }
  }

  /*  Rule 6: IF M = 0,   */
  r6 = (boxscores[0] == 0.0);

  /*  THEN: CDR = 0 unless there is impairment (0.5 or greater) in */
  /*  two or more secondary categories, in which case CDR = 0.5. */
  if (r6) {
    CDR_size[0] = 1;
    CDR_size[1] = 1;
    CDR_data[0] = 0.0;
    if (nhi > 2.0) {
      CDR_size[0] = 1;
      CDR_size[1] = 1;
      CDR_data[0] = 0.5;
    }
  }

  /*  Output rules checking */
  /*  0 if the rule was false; 1 if the rule is TRUE; */
  /*  If more than one rule was true, the latest defined the final CDR. */
  rules[0] = r1;
  rules[1] = r2;
  rules[2] = r3;
  rules[3] = r4;
  rules[4] = r5;
  rules[5] = r6;

  /*  Check Exceptions */
  /*  Although applicable to most Alzheimer's disease situations, these rules do */
  /*  not cover all possible scoring combinations.  */
  /*  Unusual circumstances occur occasionally in Alzheimer's disease and may */
  /*  be expected in non-Alzheimer dementia as well, and are scored as follows:  */
  /*  Exception (1). With ties in the secondary categories on one side of M, */
  /*  choose the tied scores closest to M for CDR (e.g., M and another */
  /*  secondary category = 3, two secondary categories = 2, and two secondary */
  /*  categories = 1; CDR = 2)    */
  /* %%% EMM: This exception was covered in the decision of Rule 2. */
  /* if ~r2, N=1:5; end %exc1 = false; % Default value if Rule 2 was not met. */
  if (!rtIsNaN(N[0])) {
    p = 1;
  } else {
    p = 0;
    k = 2;
    exitg1 = false;
    while ((!exitg1) && (k < 6)) {
      if (!rtIsNaN(N[k - 1])) {
        p = k;
        exitg1 = true;
      } else {
        k++;
      }
    }
  }

  if (p == 0) {
    absxk = N[0];
  } else {
    absxk = N[p - 1];
    i0 = p + 1;
    for (k = i0; k < 6; k++) {
      x = N[k - 1];
      if (absxk < x) {
        absxk = x;
      }
    }
  }

  for (i0 = 0; i0 < 5; i0++) {
    b_boxscores[i0] = (N[i0] == absxk);
  }

  /*  Exception 1. check if there are ties in sec. cat. */
  /*  Exception (2). When only one or two secondary categories are given the */
  /*  same score as M, CDR = M as long as no more than two secondary categories */
  /*  are on either side of M.   */
  if ((neq <= 2.0) && (nhi <= 2.0) && (nlo <= 2.0)) {
    r5 = true;
    CDR_size[0] = 1;
    CDR_size[1] = 1;
    CDR_data[0] = boxscores[0];
  } else {
    r5 = false;
  }

  /*  Exception (3). When M = 1 or greater, CDR cannot be 0; in this */
  /*  circumstance, CDR = 0.5 when the majority of secondary categories are 0.  */
  if (boxscores[0] >= 1.0) {
    pEnd = CDR_size[1];
    for (i0 = 0; i0 < pEnd; i0++) {
      x_data[i0] = (CDR_data[i0] == 0.0);
    }

    r6 = true;
    pEnd = 1;
    exitg1 = false;
    while ((!exitg1) && (pEnd <= CDR_size[1])) {
      if (!x_data[pEnd - 1]) {
        r6 = false;
        exitg1 = true;
      } else {
        pEnd++;
      }
    }

    if (r6) {
      r6 = true;
      CDR_size[0] = 1;
      CDR_size[1] = 1;
      CDR_data[0] = 0.5;
    } else {
      r6 = false;
    }
  } else {
    r6 = false;
  }

  /*  Output exceptions checking */
  /*  0 if the exception is FALSE; 1 if the exception is TRUE; */
  /*  If more than one exception was true, the latest defined the final CDR. */
  excep[0] = ((((b_boxscores[0] + b_boxscores[1]) + b_boxscores[2]) +
               b_boxscores[3]) + b_boxscores[4] > 1);
  excep[1] = r5;
  excep[2] = r6;
}

/* End of code generation (getglobalCDR.c) */
