package fr.medes.android.database.sqlite.stmt.query;

import java.util.List;

/**
 * Internal interfaces which define a comparison operation.
 *
 * @author Medes-IMPS
 */
interface Comparison extends Clause {

	/**
	 * Return the column-name associated with the comparison.
	 *
	 * @return The column name associated with the comparison.
	 */
	String getColumnName();

	/**
	 * Add the operation used in this comparison to the string builder.
	 *
	 * @param sb The StringBuilder to append the operation.
	 */
	void appendOperation(StringBuilder sb);

	/**
	 * Add the value of the comparison to the string builder.
	 *
	 * @param sb      The StringBuilder to append the clause statement.
	 * @param argList The list of arguments to append the arguments of the clause.
	 */
	void appendValue(StringBuilder sb, List<Object> argList);
}
