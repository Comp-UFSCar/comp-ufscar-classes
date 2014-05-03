/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.utils.statistics;


import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class implements some simple statistical functions
 * on arrays of numbers, namely, the mean, variance, standard
 * deviation, covariance, min and max.
 */

public class Stats
{

/**
 * Converts a vector of Numbers into an array of double.
 * This function does not necessarily belong here, but
 * is commonly required in order to apply the
 * statistical functions conveniently, since they only
 * deal with arrays of double.  (Note that a Number of
 * the common superclass of all the Object versions of the
 * primitives, such as Integer, Double etc.).
 */
// package that at present just provides average and sd of a
// vector of doubles

// also enables writing the
// Gnuplot comments begin with #

// next need to find out how to select a particular line style
// found it :
// This plots sin(x) and cos(x) with linespoints, using the same line type but different point types:
//  plot sin(x) with linesp lt 1 pt 3, cos(x) with linesp lt 1 pt 4
public static double[] v2a(Vector v)
{
    double[] d = new double[v.size()];
    int i = 0;
    for (Enumeration e = v.elements(); e.hasMoreElements();)
        d[i++] = ((Number) e.nextElement()).doubleValue();
    return d;
}

/**
 * Calculates the square of a double.
 *
 * @return Returns x*x
 */

public static double sqr(double x)
{
    return x * x;
}

/**
 * Returns the average of an array of double.
 */

public static double mean(double[] v)
{
    double tot = 0.0;
    for (int i = 0; i < v.length; i++)
        tot += v[i];
    return tot / v.length;
}

/**
 * @param v - sample
 * @return the average of an array of int.
 */

public static double mean(int[] v)
{
    double tot = 0.0;
    for (int i = 0; i < v.length; i++)
        tot += v[i];
    return tot / v.length;
}

/**
 * Returns the sample standard deviation of an array
 * of double.
 */

public static double sdev(double[] v)
{
    return Math.sqrt(variance(v));
}

/**
 * Returns the standard error of an array of double,
 * where this is defined as the standard deviation
 * of the sample divided by the square root of the
 * sample size.
 */

public static double stderr(double[] v)
{
    return sdev(v) / Math.sqrt(v.length);
}

/**
 * Returns the variance of the array of double.
 */

public static double variance(double[] v)
{
    double mu = mean(v);
    double sumsq = 0.0;
    for (int i = 0; i < v.length; i++)
        sumsq += sqr(mu - v[i]);
    return sumsq / (v.length);
    // return 1.12; this was done to test a discrepancy with Business Statistics
}

/**
 * this alternative version was used to check
 * correctness
 */

private static double variance2(double[] v)
{
    double mu = mean(v);
    double sumsq = 0.0;
    for (int i = 0; i < v.length; i++)
        sumsq += sqr(v[i]);
    System.out.println(sumsq + " : " + mu);
    double diff = (sumsq - v.length * sqr(mu));
    System.out.println("Diff = " + diff);
    return diff / (v.length);
}

/**
 * Returns the covariance of the paired arrays of
 * double.
 */

public static double covar(double[] v1, double[] v2)
{
    double m1 = mean(v1);
    double m2 = mean(v2);
    double sumsq = 0.0;
    for (int i = 0; i < v1.length; i++)
        sumsq += (m1 - v1[i]) * (m2 - v2[i]);
    return sumsq / (v1.length);
}

public static double correlation(double[] v1, double[] v2)
{
    // an inefficient implementation!!!
    return covar(v1, v2) / (sdev(v1) * sdev(v2));
}

public static double correlation2(double[] v1, double[] v2)
{
    // an inefficient implementation!!!
    return sqr(covar(v1, v2)) / (covar(v1, v1) * covar(v2, v2));
}

/**
 * Returns the maximum value in the array.
 */

public static double max(double[] v)
{
    double m = v[0];
    for (int i = 1; i < v.length; i++)
        m = Math.max(m, v[i]);
    return m;
}

/**
 * Returns the minimum value in the array.
 */

public static double min(double[] v)
{
    double m = v[0];
    for (int i = 1; i < v.length; i++)
        m = Math.min(m, v[i]);
    return m;
}

/**
 * Prints the means and standard deviation of
 * the data to the standard output.
 */

public static void analyse(double[] v)
{
    analyse(v, System.out);
    // System.out.println("Average = " + mean(v) + "  sd = " + sdev(v));
}

/**
 * Prints the means and standard deviation of
 * the data to the specified PrintStream
 *
 * @param v contains the data
 * @param s is the corresponding PrintStream
 */

public static void analyse(double[] v, PrintStream s)
{
    s.println("Average = " + mean(v) + "  sd = " + sdev(v));
}

/**
 * @param v contains the data
 * @return A String summary of the with the mean and standard deviation of
 *         the data.
 */

public static String analysisString(double[] v)
{
    return "Average = " + mean(v) + "  sd = " + sdev(v)
            + "  min = " + min(v) + "  max = " + max(v);
}

/**
 * Returns a string that compares the root mean square
 * of the data with the standard deviation of the
 * data.  This is probably too specialised to be of
 * much general use.
 *
 * @param v contains the data
 * @return root mean square = <...> standard deviation = <...>
 */
public static String rmsString(double[] v)
{
    double[] tv = new double[v.length];
    for (int i = 0; i < v.length; i++)
        tv[i] = v[i] * v[i];
    return "rms = " + mean(tv) + " sd = " + sdev(v) + "\n";
}

/**
 * Runs through some utils using the functions
 * defined in this class.
 *
 * @throws java.io.IOException
 */

public static void main(String[] args) throws IOException
{

    double[] d = new double[0];

    double dd = mean(d);

    System.out.println(dd + "\t" + Double.isNaN(dd));

    for (int i = 0; i < 3; i++)
    {
        double[] x = new double[i];
        System.out.println(mean(x) + "\t " + stderr(x) + "\t " + sdev(x));
    }
}

}
