package com.omdb.movies.network.transformers

abstract class Transformer<ResponseDaoClass, LocalDataClass> {
    abstract fun transform(responseDao:ResponseDaoClass):LocalDataClass
}