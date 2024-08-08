import {cn} from "@/lib/utils";

export default function FeatureCard(
  {
    feature
  }
    : Readonly<{
    feature: FeatureType
  }>
) {


  //  RENDER

  return (
    <div
      className={cn(
        "flex flex-col lg:border-r  py-10 relative group/feature animate__animated animate__fadeIn animate__slow animate__delay-2s",
        (feature.index === 0 || feature.index === 4) && "lg:border-l",
        feature.index < 4 && "lg:border-b"
      )}
    >
      {feature.index < 4 && (
        <div
          className="opacity-0 group-hover/feature:opacity-100 transition duration-200 absolute inset-0 h-full w-full bg-gradient-to-t from-primary/5 to-transparent pointer-events-none"/>
      )}
      {feature.index >= 4 && (
        <div
          className="opacity-0 group-hover/feature:opacity-100 transition duration-200 absolute inset-0 h-full w-full bg-gradient-to-b from-primary/5 to-transparent pointer-events-none"/>
      )}
      {
        feature.icon ?
          <div className="mb-4 relative z-10 px-10 text-primary">
            {feature.icon}
          </div> : null
      }
      <div className="text-lg font-bold mb-2 relative z-10 px-10">
        <div className="absolute left-0 inset-y-0 h-6 group-hover/feature:h-8 w-1 rounded-tr-full rounded-br-full bg-neutral-300 group-hover/feature:bg-primary transition-all duration-200 origin-center"/>
        <span
          className="group-hover/feature:translate-x-2 transition duration-200 inline-block text-primary">
          {feature.title}
        </span>
      </div>
      <p className="text-sm text-muted-foreground max-w-xs relative z-10 px-10">
        {feature.description}
      </p>
    </div>
  )
}