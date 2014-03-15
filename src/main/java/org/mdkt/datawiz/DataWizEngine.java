/**
 *
 */
package org.mdkt.datawiz;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mdkt.datawiz.annotation.TadahDataSpec;
import org.mdkt.datawiz.annotation.TadahDataSpecFile;
import org.mdkt.datawiz.annotation.TadahDataSpecList;
import org.mdkt.datawiz.model.DataSpecModel;
import org.mdkt.datawiz.model.DataSpecModelBuilder;
import org.mdkt.datawiz.model.MethodSpecModel;

/**
 * The core engine that does the magic
 *
 * @author trung
 *
 */
public class DataWizEngine {

	private static final Logger logger = Logger.getLogger(DataWizEngine.class);

	private ThreadLocal<Map<String, Object>> contextThreadLocal = null;

	private DataWizInstanceProvider instanceProvider = null;

	private static DataWizEngine singleton = null;

	private DataWizEngine(DefaultDataWizInstanceProvider instanceProvider) {
		this.contextThreadLocal = new ThreadLocal<Map<String,Object>>();
		this.instanceProvider = instanceProvider;
	}

	public static DataWizEngine instance() {
		return instance(new DefaultDataWizInstanceProvider());
	}

	/**
	 * @param instanceProvider
	 * @return
	 */
	private static DataWizEngine instance(
			DefaultDataWizInstanceProvider instanceProvider) {
		if (singleton == null) {
			singleton = new DataWizEngine(instanceProvider);
		}
		return singleton;
	}

	/**
	 * Read annotations from the provided method and class. Run the data wiz handler
	 *
	 * @param testClazz
	 * @param testMethodName
	 * @throws DataWizException
	 */
	public void prepare(Class<?> testClazz,
			String testMethodName) {
		checkContext(true);
		try {
			Method testMethod = testClazz.getDeclaredMethod(testMethodName);
			DataSpecModel model = DataSpecModelBuilder.create()
					.from(testMethod.getAnnotation(TadahDataSpec.class))
					.from(testMethod.getAnnotation(TadahDataSpecList.class))
					.from(testMethod.getAnnotation(TadahDataSpecFile.class), testMethod)
					.build();
			execute(model);
		} catch (Exception e) {
			throw new DataWizException("Unable to prepare the data due to " + e.toString(), e);
		}
	}

	/**
	 * @param model
	 */
	public void execute(DataSpecModel model) throws Exception {
		List<MethodSpecModel> methods = model.getMethods();
		for (MethodSpecModel m : methods) {
			logger.debug("Execute method " + m);
			Method executingMethod = m.getHandler().getDeclaredMethod(m.getMethodName(), m.getMethodArgumentTypes().toArray(new Class<?>[0]));
			AbstractDataWiz service = instanceProvider.get(m.getHandler());
			executingMethod.invoke(service, m.getMethodArgumentValues().toArray(new Object[0]));
			contextThreadLocal.get().putAll(model.getContext());
			contextThreadLocal.get().putAll(service.getLocalContext());
		}
	}

	private void checkContext(boolean reset) {
		if (contextThreadLocal.get() == null) {
			contextThreadLocal.set(new HashMap<String, Object>());
		}
		if (reset) {
			contextThreadLocal.get().clear();
		}
	}

	/**
	 * Obtain variable binding value mentioned in the data spec phrase
	 * or dynamically created during prepration
	 *
	 * @return
	 */
	public Object getContextValue(String variable) {
		checkContext(false);
		return contextThreadLocal.get().get(variable);
	}

	/**
	 * Clean up the context
	 */
	public void cleanUp() {
		checkContext(true);
	}
}
