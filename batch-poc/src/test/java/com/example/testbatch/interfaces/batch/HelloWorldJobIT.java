package com.example.testbatch.interfaces.batch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.cli.WithCliCommand;
import org.seedstack.seed.testing.Arguments;
import org.seedstack.seed.testing.junit4.internal.JUnit4Runner;

@RunWith(JUnit4Runner.class)
public class HelloWorldJobIT {
	@Test
	@WithCliCommand(command = "run-job", expectedStatusCode = 0)
	@Arguments({"--job", "helloWorldJob"})
	public void helloWorld() throws Exception {
		assertTrue("Job executed", true);
	}
}
