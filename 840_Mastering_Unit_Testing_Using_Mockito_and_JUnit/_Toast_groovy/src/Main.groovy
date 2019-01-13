
Map<String, List<BigDecimal>> map = [a:[1 as BigDecimal,2 as BigDecimal,3 as BigDecimal,5 as BigDecimal], b:[1 as BigDecimal,2 as BigDecimal,3 as BigDecimal,5 as BigDecimal], c:[1 as BigDecimal,2 as BigDecimal,3 as BigDecimal,5 as BigDecimal]];

println map.values().flatten().sum().class