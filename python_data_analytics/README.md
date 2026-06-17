# Introduction

London Gift Shop (LGS) is a UK-based online retailer that specializes in selling giftware products. Despite its long operating history and established customer base, the company has recently experienced challenges with revenue growth. To better understand customer purchasing behavior and identify opportunities for business growth, the LGS marketing team wants to gain deeper insights into customer activity, sales trends, and customer value.

LGS has partnered with Jarvis to develop a proof of concept that analyzes historical retail transaction data and provides actionable business insights. The goal of this project is to help the marketing team improve customer retention, develop more effective marketing strategies, and identify high-value customer segments that can contribute to increased revenue.

This project was implemented using Python in a Jupyter Notebook environment. Pandas DataFrames were used for data cleaning, transformation, and analysis, while NumPy was used for mathematical and statistical operations. Matplotlib was used to create visualizations that highlight sales trends, customer activity, revenue growth, and customer segmentation. The resulting analysis provides a data-driven view of customer behavior and business performance, helping LGS make informed marketing and sales decisions.

# Implementaion
## Project Architecture

The project begins by loading retail transaction data from PostgreSQL or CSV files into Pandas DataFrames. The data is then cleaned, validated, and transformed before performing analytical calculations and generating visualizations. The resulting insights are used by business stakeholders and marketing teams to support decision-making.
![Architecture Diagram](DataAnalticsDiagram.png)
## Data Analytics and Wrangling

Jupyter Notebook:

```text
./retail_data_analytics_wrangling.ipynb
```

The retail transaction data was loaded from both PostgreSQL and CSV sources into Pandas DataFrames for analysis. Data wrangling tasks included data type conversion, column standardization, missing value inspection, and validation of transactional records before performing analytics.

Several business-focused analyses were conducted throughout the project:

* Total Invoice Amount Distribution to understand customer spending patterns and identify outliers.
* Invoice Amount Distribution Across the First 85 Quantiles to analyze spending behavior after removing extreme outliers.
* Monthly Placed and Cancelled Orders to measure purchasing activity and cancellation trends over time.
* Monthly Sales Analysis to evaluate overall revenue performance.
* Monthly Sales Growth Analysis to measure month-over-month revenue changes.
* Monthly Active Users Analysis to track customer engagement.
* New vs Existing Customer Analysis to understand customer acquisition and retention trends.
* RFM (Recency, Frequency, Monetary) Analysis to evaluate customer value.
* RFM Segmentation to classify customers into marketing segments based on purchasing behavior.

The insights generated from these analyses help LGS better understand customer behavior, identify high-value customer segments, monitor sales performance, and evaluate customer retention. The marketing team can use these findings to create targeted campaigns, improve customer engagement, and increase revenue through data-driven decision making.


# Improvements

- Create an interactive dashboard using Power BI, Tableau, or Plotly Dash to allow stakeholders to explore business metrics in real time.
- Develop machine learning models to predict customer churn, customer lifetime value, and future sales trends.
- Build a scalable data pipeline using PostgreSQL, Apache Spark, and automated ETL workflows to support larger datasets and production-level analytics.
