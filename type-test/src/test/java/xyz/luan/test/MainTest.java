package xyz.luan.test;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class MainTest {
	@Test
	public void simpleTest() {
		assertThat(2 + 2).isEqualTo(4);
	}
}
