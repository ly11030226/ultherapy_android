//
// Created by 秋 on 2022/9/2
// email 1029496119@qq.com
//

#ifndef SENTINEL_HPP
#define SENTINEL_HPP

#include <string>
#include "rsa.h"

namespace AppSentinel {

    using namespace std;

    class Sentinel {
    private:
        Sentinel();

        ~Sentinel();

        static Sentinel *p;
        string token = "";

    public:
        static Sentinel *instance();

        string generateToken();

        bool verifyToken(string token);
    };
}

#endif //SENTINEL_HPP
