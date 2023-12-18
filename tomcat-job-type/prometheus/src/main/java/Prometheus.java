import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;
import javax.naming.Name;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
// import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;

public class Prometheus implements ObjectFactory {

	public Object getObjectInstance(Object obj, Name name2, Context nameCtx, Hashtable environment)
		      throws NamingException {

// 		JvmMetrics.builder().register(PrometheusRegistry.defaultRegistry);

        return PrometheusRegistry.defaultRegistry;
    }

}

