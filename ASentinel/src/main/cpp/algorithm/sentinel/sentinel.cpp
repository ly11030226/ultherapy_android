//
// Created by 秋 on 2022/9/2
// email 1029496119@qq.com
//


#include <string>
#include <stdlib.h>

#include "sentinel.hpp"
#include "rsa_tools.hpp"


using namespace std;
using namespace SentinelCommon;

namespace AppSentinel {
    static inline rsa_key_data_def token_generate_key() {
        rsa_key_data_def rsa_key = {
                .E = "10001",
                .P = "",
                .Q = "",
                .DP = "",
                .DQ = "",
                .QP = "",
                .N = "A2BECBF166950F119F32FDEE8B296119A114B174D75D9591BA956869CE43EB9FF6ADD17D2EAE67BE88AF0848F0DBDED182AC8ECB600DF1C67B1F7FA3B1BAD8F3DDEBF333A3D05959F1725DABC7E0E6C4BB89D7E831913B8E15D593048CFAA58B174A6E95DB3EA6047985F141C98BD4F32BDE6C37EBA36E8C04EEDE524B7EE0C5",
                .D = "",
        };
        return rsa_key;
    }

    Sentinel::Sentinel() {

    }

    Sentinel::~Sentinel() {
    }

    Sentinel *Sentinel::p = new Sentinel();

    Sentinel *Sentinel::instance() {
        return p;
    }

    string Sentinel::generateToken() {
        unsigned char token_c_str[KEY_LEN * 2 + 1] = {'\0'};
        rand_hex_string(token_c_str, KEY_LEN * 2);
        token_c_str[0] = '0';
        token_c_str[KEY_LEN * 2] = '\0';
        string token = (char *) token_c_str;
        string ciphertext;
        int ret = rsa_public(token_generate_key(), token, &ciphertext);
        if (ret) {
            this->token = "";
            return "";
        } else {
            this->token = token;
            return ciphertext;
        }
    }

    bool Sentinel::verifyToken(string token) {
        transform(token.begin(), token.end(), token.begin(), ::toupper);
        bool verify = (this->token == token);
        return verify;
    }

}
