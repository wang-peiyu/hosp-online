package com.onlinehosp;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;

import java.util.Arrays;
import java.util.UUID;

public class OssTest {
    public static void main(String[] args) throws Exception {
      Person person = new Person();
      person.a=5;
        System.out.println(person);

    }
}
