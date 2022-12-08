The following project contains an implementation of inner product based on DDH and Paillier.

The folder inner contains implementation for inner product based on DDH where the computations have been abstracted out into two persons,
Alice and Bob, trying to compute bloodtype compatibility function for their inputs. Alice is receiver and Bob is donor.

Likewise the folder paillier contains implementation for inner product based on Paillier.

Test for correctness both DDH and Paillier can be found under InnerProductTest and PaillierTest respectively.
Furthermore, benchmark of the two constructions can be found under BenchmarkCrypto