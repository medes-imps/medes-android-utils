package fr.medes.android.database.sqlite.stmt.query;

/**
 * Internal interface which defines a clause that consumes a future clause. This allows us to do:
 * <p/>
 * <pre>
 * where.not();
 * where.eq(&quot;id&quot;, 1234);
 * </pre>
 * <p/>
 * and
 * <p/>
 * <pre>
 * where.eq(&quot;id&quot;, 1234);
 * where.and();
 * where.gt(&quot;age&quot;, 44);
 * </pre>
 *
 * @author Medes-IMPS
 */
public interface NeedsFutureClause extends Clause {

	/**
	 * Set the right side of the binary operation.
	 *
	 * @param right The right clause of the operation.
	 */
	void setMissingClause(Clause right);
}
