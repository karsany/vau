# VAU

Data Vault data modeling tool and ETL generator for Oracle Databases

## Introduction

Model your datawarehouse DW layer, write your ETL/ELT logic with simple 
SQL SELECT commands, generate table scripts, stored procedures and
documentation.

VAU is a datawarehouse source code generator supporting Oracle Databases.
It helps generating the DW layer in a datawarehouse, based on Data Vault
methodology.

VAU is licenced under the BSD licence, and is 100% free.

## Features

A few things you can do with VAU:

  * model DW layer: entities/hubs, sattelites, links and reference tables
  * map the Stage layer to DW layer with SQL SELECT or with VAU's simplemap
  * generate table scripts and sequences for DW layer
  * generate loader procedures for populating the DW tables
  * generate example mapping
  * generate data model diagram and CSV
  * generate table and column lineage CSVs
  * you can version your DW model and logic with GIT, SVN, etc.

## Feedback

If you notice any bugs in the app, see some code that can be improved,
or have features you would like to be added, please create an issue!

If you want to open a PR that fixes a bug or adds a feature, then we can't thank you enough!
It is definitely appreciated if an issue has been created before-hand so it can be discussed first.

## Build Process

Currently -- due to heavy development -- there is no released versions yet.
If you want to build VAU, you can do it by running nightly_snapshot.bat from tools directory.
It compiles the binary version: vau-bin-yyyymmdd-hhmmss.zip

Prerequisites for build are: jdk8 or above, maven, git, cat, grep, sed.

If you unzip the build somewhere, there will be a vau.cmd in it.

Compile the example from directory `examples/oracle-hr` with:

    vau.cmd clean compile doc
	
