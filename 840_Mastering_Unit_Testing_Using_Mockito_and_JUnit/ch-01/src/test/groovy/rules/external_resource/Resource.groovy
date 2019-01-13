package rules.external_resource

import groovy.transform.CompileStatic

class Resource {
  void open() { println 'Opened' }
  void close() { println 'Closed' }
  double get() { Math.random() }
}
