/*
 * Paprika - Detection of code smells in Android application
 *     Copyright (C)  2016  Geoffrey Hecht - INRIA - UQAM - University of Lille
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package paprika.query.neo4j.queries.antipatterns.fuzzy;

import paprika.query.neo4j.queries.QueryPropertiesReader;

/**
 * Created by Geoffrey Hecht on 14/08/15.
 */
public class HeavyAsyncTaskStepsQuery extends HeavySomethingQuery {

    public static final String KEY = "HAS";

    public HeavyAsyncTaskStepsQuery(QueryPropertiesReader reader) {
        super(KEY, reader);
    }

    @Override
    public String getQuery(boolean details) {
        String query = getAsyncStepNodes(reader.get("Heavy_class_veryHigh_noi"),
                reader.get("Heavy_class_veryHigh_cc"));
        query += "RETURN m.app_key as app_key, m.full_name as full_name, labels(m)[0] as `LABEL[0]`";
        return query;
    }

    @Override
    public String getFuzzyQuery(boolean details) {
        String query = getAsyncStepNodes(reader.get("Heavy_class_high_noi"),
                reader.get("Heavy_class_high_cc"));
        query += "RETURN m.full_name as full_name, m.app_key as app_key, m.cyclomatic_complexity as cyclomatic_complexity,\n" +
                "m.number_of_instructions as number_of_instructions, labels(m)[0] as `LABEL[0]`";
        return query;
    }

    private String getAsyncStepNodes(double noiThreshold, double ccThreshold) {
        return " MATCH (c:Class{parent_name:'android.os.AsyncTask'})-[:CLASS_OWNS_METHOD]->(m:Method)\n" +
                "WHERE (m.name='onPreExecute' OR m.name='onProgressUpdate' OR m.name='onPostExecute')\n" +
                "   AND m.number_of_instructions > " + noiThreshold + "\n" +
                "   AND m.cyclomatic_complexity > " + ccThreshold + "\n";
    }

}
