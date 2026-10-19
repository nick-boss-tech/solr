#include <stddef.h>
void *eglGetCurrentDisplay(void) { return NULL; }
void *eglGetProcAddress(const char *p) { (void)p; return NULL; }
const char *eglQueryString(void *d, int n) { (void)d; (void)n; return NULL; }
