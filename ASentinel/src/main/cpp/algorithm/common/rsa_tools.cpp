//
//  rsa_tools.cpp
//  AppSentinel
//
//  Created by GuHaiJun on 2022/9/3.
//

#include "rsa_tools.hpp"

namespace SentinelCommon {

void rand_hex_string(unsigned char *output, int len) {
    char hex_characters[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    for(int i = 0; i < len; i++) {
        output[i]=hex_characters[rand()%16];
    }
    output[len]=0;
}

void rsa_init_with_key_data(rsa_context *ctx, rsa_key_data_def key_data) {
    rsa_init( ctx, RSA_PKCS_V15, 0 );
    ctx->len = KEY_LEN;
    mpi_read_string( &(ctx->E) , 16, key_data.E.c_str());
    mpi_read_string( &(ctx->P) , 16, key_data.P.c_str());
    mpi_read_string( &(ctx->Q) , 16, key_data.Q.c_str());
    mpi_read_string( &(ctx->DP) , 16, key_data.DP.c_str());
    mpi_read_string( &(ctx->DQ) , 16, key_data.DQ.c_str());
    mpi_read_string( &(ctx->QP) , 16, key_data.QP.c_str());
    mpi_read_string( &(ctx->N) , 16, key_data.N.c_str());
    mpi_read_string( &(ctx->D) , 16, key_data.D.c_str());
}

int rsa_private(rsa_key_data_def private_key, string input, string *output) {
    int ret = 0;
    unsigned char rsa_input_char_hex[KEY_LEN * 2 + 1] = {'\0'};
    unsigned char rsa_output_char_hex[KEY_LEN * 2 + 1] = {'\0'};
    unsigned char rsa_input_char[KEY_LEN] = {0};
    unsigned char rsa_output_char[KEY_LEN] = {0};
    string _output = "";
    rsa_context ctx;
    rsa_init_with_key_data(&ctx, private_key);
    memcpy(rsa_input_char_hex, input.c_str(), KEY_LEN * 2);
    rsa_input_char_hex[KEY_LEN * 2] = '\0';
    ret = unhexify(rsa_input_char, sizeof(rsa_input_char), (const char *)rsa_input_char_hex);
    if (ret) {
        goto end;
    }
    ret = rsa_private(&ctx, rsa_input_char, rsa_output_char);
    if (ret) {
        goto end;
    }
    hexify(rsa_output_char_hex, rsa_output_char, KEY_LEN);
    rsa_output_char_hex[KEY_LEN * 2] = '\0';
    _output = (char *)rsa_output_char_hex;
    transform(_output.begin(),_output.end(),_output.begin(),::toupper);
    *output = _output;
end:
    rsa_free(&ctx);
    return ret;
}

int rsa_public(rsa_key_data_def public_key, string input, string *output) {
    int ret = 0;
    unsigned char rsa_output_char_hex[KEY_LEN * 2 + 1] = {'\0'};
    unsigned char rsa_input_char_hex[KEY_LEN * 2 + 1] = {'\0'};
    unsigned char rsa_output_char[KEY_LEN] = {0};
    unsigned char rsa_input_char[KEY_LEN] = {0};
    string _output = "";
    rsa_context ctx;
    rsa_init_with_key_data(&ctx, public_key);
    memcpy(rsa_input_char_hex, input.c_str(), KEY_LEN * 2);
    rsa_output_char_hex[KEY_LEN * 2] = '\0';
    ret = unhexify(rsa_input_char, sizeof(rsa_input_char), (const char *)rsa_input_char_hex);
    if (ret) {
        goto end;
    }
    ret = rsa_public(&ctx, rsa_input_char, rsa_output_char);
    if (ret) {
        goto end;
    }
    hexify(rsa_output_char_hex, rsa_output_char, KEY_LEN);
    rsa_output_char_hex[KEY_LEN * 2] = '\0';
    _output = (char *)rsa_output_char_hex;
    transform(_output.begin(),_output.end(),_output.begin(),::toupper);
    *output = _output;
end:
    rsa_free(&ctx);
    return ret;
}

}
