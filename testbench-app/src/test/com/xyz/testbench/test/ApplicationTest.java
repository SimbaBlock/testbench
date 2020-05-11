package com.xyz.testbench.test;


import com.xyz.testbench.app.GunsApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { GunsApplication.class })
@WebAppConfiguration
public class ApplicationTest {




}
