//
//  helpers.c
//  AppSentinelTools
//
//  Created by GuHaiJun on 2022/6/20.
//

#include "helpers.h"

#include "config.h"

#ifdef _MSC_VER
#include <basetsd.h>
typedef UINT32 uint32_t;
#else
#include <inttypes.h>
#endif

#include <stdlib.h>
#include <string.h>
#include <assert.h>

static int ascii2uc(const char c, unsigned char *uc)
{
    if( ( c >= '0' ) && ( c <= '9' ) )
        *uc = c - '0';
    else if( ( c >= 'a' ) && ( c <= 'f' ) )
        *uc = c - 'a' + 10;
    else if( ( c >= 'A' ) && ( c <= 'F' ) )
        *uc = c - 'A' + 10;
    else
        return( -1 );

    return( 0 );
}

int unhexify(unsigned char *obuf, size_t olen, const char *ibuf)
{
    unsigned char uc, uc2;

    int len = (int)strlen( ibuf );

    /* Must be even number of bytes. */
    if ( ( len ) & 1 )
        return( -1 );
    len /= 2;

    if ( (len) > olen )
        return( -1 );

    while( *ibuf != 0 )
    {
        if ( ascii2uc( *(ibuf++), &uc ) != 0 )
            return( -1 );

        if ( ascii2uc( *(ibuf++), &uc2 ) != 0 )
            return( -1 );

        *(obuf++) = ( uc << 4 ) | uc2;
    }

    return( 0 );
}

void hexify(unsigned char *obuf, const unsigned char *ibuf, int ilen)
{
    unsigned char l, h;

    while( ilen != 0 )
    {
        h = *ibuf / 16;
        l = *ibuf % 16;

        if( h < 10 )
            *obuf++ = '0' + h;
        else
            *obuf++ = 'a' + h - 10;

        if( l < 10 )
            *obuf++ = '0' + l;
        else
            *obuf++ = 'a' + l - 10;

        ++ibuf;
        ilen--;
    }
}
